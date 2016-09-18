package com.gnefedev.jee.testing.offline.scanner

import com.gnefedev.jee.testing.Config
import com.gnefedev.jee.testing.offline.getInterceptors
import com.gnefedev.jee.testing.offline.toList
import org.apache.activemq.command.ActiveMQQueue
import org.apache.activemq.command.ActiveMQTopic
import org.springframework.beans.MutablePropertyValues
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.config.RuntimeBeanReference
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.beans.factory.support.GenericBeanDefinition
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.jms.listener.DefaultMessageListenerContainer
import javax.annotation.Resource
import javax.ejb.MessageDriven
import javax.ejb.Stateful
import javax.ejb.Stateless
import javax.enterprise.inject.Alternative
import javax.jms.Queue
import javax.jms.Topic
import javax.validation.ValidationException
import javax.xml.parsers.DocumentBuilderFactory

private fun Class<*>.isStateful() = isAnnotationPresent(Stateful::class.java)
private fun Class<*>.isStateless() = isAnnotationPresent(Stateless::class.java)
private fun Class<*>.isMdb() = isAnnotationPresent(MessageDriven::class.java)
private fun Class<*>.isTest() = TestsFilter.isTestClass(this)

/**
 * Created by gerakln on 04.09.16.
 */
internal class JeeScanner : BeanDefinitionRegistryPostProcessor {
    private val registeredAlternatives: Set<String> by lazy {
        return@lazy DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(javaClass.getResourceAsStream("/META-INF/beans.xml"))
                .documentElement
                .childNodes
                .toList()
                .first { it.nodeName == "alternatives" }
                .childNodes
                .toList()
                .filter { it.nodeName == "class" }
                .map { it.textContent }
                .toSet()
    }


    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        val scanner = ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(TestsFilter())
        scanner.addIncludeFilter(AnnotationTypeFilter(Stateless::class.java))
        scanner.addIncludeFilter(AnnotationTypeFilter(Stateful::class.java))
        scanner.addIncludeFilter(AnnotationTypeFilter(MessageDriven::class.java))
        val packageToScan = Config.packageToScan
        if (packageToScan.isBlank()) {
            throw ValidationException("packageToScan is required in javaee-testing.properties")
        }
        for (definition in scanner.findCandidateComponents(packageToScan)) {
            val beanClass = Class.forName(definition.beanClassName)
            if (beanClass.isMdb()) {
                registerMdb(beanClass, registry)
            }
            registerBean(definition, registry, beanClass)
            registerResources(beanClass, registry)
        }
    }

    private fun registerResources(beanClass: Class<*>, registry: BeanDefinitionRegistry) {
        beanClass
                .declaredFields
                .filter { it.isAnnotationPresent(Resource::class.java) }
                .forEach { it ->
                    val resource = it.getAnnotation(Resource::class.java)
                    when (it.type) {
                         Queue::class.java -> registerDestination(resource, registry, ActiveMQQueue::class.java)
                         Topic::class.java -> registerDestination(resource, registry, ActiveMQTopic::class.java)
                    }
                }
    }

    private fun registerDestination(queue: Resource, registry: BeanDefinitionRegistry, implementation: Class<*>) {
        val beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(implementation)
                .addConstructorArgValue(queue.name)
                .beanDefinition
        registry.registerBeanDefinition(queue.name, beanDefinition)
    }

    private fun registerMdb(mdb: Class<*>, registry: BeanDefinitionRegistry) {
        val listenerDefinition = GenericBeanDefinition()
        listenerDefinition.beanClass = DefaultMessageListenerContainer::class.java

        val values = MutablePropertyValues()
        val destinationName = mdb
                .getAnnotation(MessageDriven::class.java)
                .activationConfig
                .filter { it.propertyName == "destination" }
                .map { it.propertyValue }
                .first()
        values.addPropertyValue("destination", RuntimeBeanReference(destinationName))
        values.addPropertyValue("messageListener", RuntimeBeanReference(mdb.name))
        values.addPropertyValue("connectionFactory", RuntimeBeanReference("connectionFactory"))
        values.addPropertyValue("transactionManager", RuntimeBeanReference("transactionManager"))
        values.addPropertyValue("sessionTransacted", true)

        listenerDefinition.propertyValues = values
        listenerDefinition.initMethodName = "start"
        listenerDefinition.destroyMethodName = "stop"
        registry.registerBeanDefinition(mdb.name + "Listener", listenerDefinition)
    }

    private fun registerBean(definition: BeanDefinition, registry: BeanDefinitionRegistry, beanClass: Class<*>) {
        if (beanClass.isTest() || beanClass.isStateless()) {
            definition.scope = BeanDefinition.SCOPE_PROTOTYPE
        } else if (beanClass.isStateful()) {
            definition.scope = "test"
        }
        if (isValidBean(beanClass)) {
            if (registeredAlternatives.contains(beanClass.name)) {
                definition.isPrimary = true
            }

            registry.registerBeanDefinition(chooseName(beanClass), definition)

            registerInterceptors(registry, beanClass)
        }
    }

    private fun isValidBean(candidate: Class<*>): Boolean {
        return !candidate.isAnnotationPresent(Alternative::class.java)
                || registeredAlternatives.contains(candidate.name)
    }

    private fun chooseName(candidateClass: Class<*>): String? {
        if (candidateClass.isStateful()) {
            val annotation = candidateClass.getAnnotation(Stateful::class.java)
            return getName(candidateClass, annotation.name)
        } else if (candidateClass.isStateless()) {
            val annotation = candidateClass.getAnnotation(Stateless::class.java)
            return getName(candidateClass, annotation.name)
        } else if (candidateClass.isMdb()) {
            return candidateClass.name
        } else {
            error("Not supported annotation")
        }
    }

    private fun getName(candidateClass: Class<*>, possibleName: String): String {
        if (possibleName.isBlank()) {
            return candidateClass.simpleName
        } else {
            return possibleName
        }
    }

    private fun registerInterceptors(registry: BeanDefinitionRegistry, candidateClass: Class<*>) {
        candidateClass
                .methods
                .filter { it.declaringClass != Any::class.java }
                .flatMap { it.getInterceptors() }
                .toSet()
                .filter { !registry.containsBeanDefinition(it.name) }
                .map { BeanDefinitionBuilder.rootBeanDefinition(it) }
                .map { it.setScope("prototype") }
                .map { it.beanDefinition }
                .forEach { registry.registerBeanDefinition(it.beanClass.name, it) }
    }

    override fun postProcessBeanFactory(configurableListableBeanFactory: ConfigurableListableBeanFactory) {
    }
}