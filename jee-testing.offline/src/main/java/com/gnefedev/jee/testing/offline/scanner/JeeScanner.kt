package com.gnefedev.jee.testing.offline.scanner

import com.gnefedev.jee.testing.Config
import com.gnefedev.jee.testing.offline.getInterceptors
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
import javax.ejb.MessageDriven
import javax.ejb.Stateful
import javax.ejb.Stateless
import javax.enterprise.inject.Alternative
import javax.validation.ValidationException

private fun Class<*>.isStateful() = isAnnotationPresent(Stateful::class.java)
private fun Class<*>.isStateless() = isAnnotationPresent(Stateless::class.java)
private fun Class<*>.isMdb() = isAnnotationPresent(MessageDriven::class.java)
private fun Class<*>.isTest() = TestsFilter.isTestClass(this)

/**
 * Created by gerakln on 04.09.16.
 */
internal class JeeScanner : BeanDefinitionRegistryPostProcessor {
    private val registerdAlternatives: Set<String> by lazy {
        return@lazy setOf("com.gnefedev.test.simple.animals.Elephant")
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
            val candidateClass = Class.forName(definition.beanClassName)
            if (candidateClass.isMdb()) {
                registerMdb(candidateClass, registry)
            }
            registerBean(definition, registry)
        }
    }

    private fun registerMdb(mdb: Class<*>, registry: BeanDefinitionRegistry) {
        val listenerDefinition = GenericBeanDefinition()
        listenerDefinition.beanClass = DefaultMessageListenerContainer::class.java

        val values = MutablePropertyValues()
        values.addPropertyValue("destination", RuntimeBeanReference("jms/queue/requestQueue"))
        values.addPropertyValue("messageListener", RuntimeBeanReference(mdb.name))
        values.addPropertyValue("connectionFactory", RuntimeBeanReference("connectionFactory"))
        values.addPropertyValue("transactionManager", RuntimeBeanReference("transactionManager"))
        values.addPropertyValue("sessionTransacted", true)

        listenerDefinition.propertyValues = values
        listenerDefinition.initMethodName = "start"
        listenerDefinition.destroyMethodName = "stop"
        registry.registerBeanDefinition(mdb.name + "Listener", listenerDefinition)
    }

    private fun registerBean(definition: BeanDefinition, registry: BeanDefinitionRegistry) {
        val candidateClass = Class.forName(definition.beanClassName)
        if (candidateClass.isTest() || candidateClass.isStateless()) {
            definition.scope = BeanDefinition.SCOPE_PROTOTYPE
        } else if (candidateClass.isStateful()) {
            definition.scope = "test"
        }
        if (isValid(candidateClass)) {
            if (registerdAlternatives.contains(candidateClass.name)) {
                definition.isPrimary = true
            }

            registry.registerBeanDefinition(chooseName(candidateClass), definition)

            registerInterceptors(registry, candidateClass)
        }
    }

    private fun isValid(candidate: Class<*>): Boolean {
        return !candidate.isAnnotationPresent(Alternative::class.java)
                || registerdAlternatives.contains(candidate.name)
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