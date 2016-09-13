package com.gnefedev.javaee.testing.offline

import com.gnefedev.javaee.testing.Config
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import javax.ejb.Stateful
import javax.ejb.Stateless
import javax.enterprise.inject.Alternative
import javax.validation.ValidationException

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
        val packageToScan = Config.packageToScan
        if (packageToScan.isBlank()) {
            throw ValidationException("packageToScan is required in javaee-testing.properties")
        }
        for (definition in scanner.findCandidateComponents(packageToScan)) {
            val candidateClass = Class.forName(definition.beanClassName)
            if (TestsFilter.isTestClass(candidateClass) || candidateClass.isAnnotationPresent(Stateless::class.java)) {
                definition.scope = "prototype"
            } else if (candidateClass.isAnnotationPresent(Stateful::class.java)) {
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
    }

    private fun isValid(candidate: Class<*>): Boolean {
        return !candidate.isAnnotationPresent(Alternative::class.java)
                || registerdAlternatives.contains(candidate.name)
    }

    private fun chooseName(candidateClass: Class<*>): String? {
        if (candidateClass.isAnnotationPresent(Stateful::class.java)) {
            val annotation = candidateClass.getAnnotation(Stateful::class.java)
            return getName(candidateClass, annotation.name)
        } else if (candidateClass.isAnnotationPresent(Stateless::class.java)) {
            val annotation = candidateClass.getAnnotation(Stateless::class.java)
            return getName(candidateClass, annotation.name)
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