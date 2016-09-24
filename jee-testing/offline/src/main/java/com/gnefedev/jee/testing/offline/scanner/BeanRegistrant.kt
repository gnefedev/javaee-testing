package com.gnefedev.jee.testing.offline.scanner

import com.gnefedev.jee.testing.offline.*
import org.apache.activemq.command.ActiveMQQueue
import org.apache.activemq.command.ActiveMQTopic
import org.springframework.beans.MutablePropertyValues
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.RuntimeBeanReference
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.GenericBeanDefinition
import org.springframework.jms.listener.DefaultMessageListenerContainer
import javax.annotation.Resource
import javax.ejb.MessageDriven
import javax.ejb.Stateful
import javax.ejb.Stateless
import javax.enterprise.inject.Alternative
import javax.jms.Destination
import javax.jms.Queue
import javax.jms.Topic
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by gerakln on 18.09.16.
 */
internal class BeanRegistrant(
        private val registry: BeanDefinitionRegistry,
        private val definition: BeanDefinition
) {
    val beanClass: Class<*> by lazy { Class.forName(definition.beanClassName) }

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


    fun register() {
        if (beanClass.isMdb()) {
            createJmsListener()
        }
        registerBean()
        registerResources()
    }

    private fun createJmsListener() {
        val listenerDefinition = GenericBeanDefinition()
        listenerDefinition.beanClass = DefaultMessageListenerContainer::class.java

        val values = MutablePropertyValues()
        val destinationName = beanClass
                .getAnnotation(MessageDriven::class.java)
                .activationConfig
                .filter { it.propertyName == "destination" }
                .map { it.propertyValue }
                .first()
        values.addPropertyValue("destination", RuntimeBeanReference(destinationName))
        values.addPropertyValue("messageListener", RuntimeBeanReference(beanClass.name))
        values.addPropertyValue("connectionFactory", RuntimeBeanReference("connectionFactory"))
        values.addPropertyValue("transactionManager", RuntimeBeanReference("transactionManager"))
        values.addPropertyValue("sessionTransacted", true)

        listenerDefinition.propertyValues = values
        listenerDefinition.initMethodName = "start"
        listenerDefinition.destroyMethodName = "stop"
        registry.registerBeanDefinition(beanClass.name + "Listener", listenerDefinition)
    }

    private fun registerResources() {
        beanClass
                .declaredFields
                .filter { it.isAnnotationPresent(Resource::class.java) }
                .filter { !registry.containsBeanDefinition(it.getAnnotation(Resource::class.java).name)}
                .forEach {
                    val resource = it.getAnnotation(Resource::class.java)
                    when (it.type) {
                        Queue::class.java -> registerDestination(resource, ActiveMQQueue::class.java)
                        Topic::class.java -> registerDestination(resource, ActiveMQTopic::class.java)
                        Destination::class.java -> error("Can't generate resource for field " + it.toString())
                    }
                }
    }

    private fun registerDestination(destination: Resource, implementation: Class<*>) {
        val beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(implementation)
                .addConstructorArgValue(destination.name)
                .beanDefinition
        registry.registerBeanDefinition(destination.name, beanDefinition)
    }

    private fun registerBean() {
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

            registerInterceptors()
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

    private fun registerInterceptors() {
        beanClass
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

}