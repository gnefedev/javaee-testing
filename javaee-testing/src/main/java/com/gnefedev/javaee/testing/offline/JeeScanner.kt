package com.gnefedev.javaee.testing.offline

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import javax.ejb.Stateful
import javax.ejb.Stateless

/**
 * Created by gerakln on 04.09.16.
 */
internal class JeeScanner : BeanDefinitionRegistryPostProcessor {
    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        val scanner = ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(TestsFilter())
        scanner.addIncludeFilter(AnnotationTypeFilter(Stateless::class.java))
        scanner.addIncludeFilter(AnnotationTypeFilter(Stateful::class.java))
        for (definition in scanner.findCandidateComponents("com.gnefedev")) {
            val candidateClass = Class.forName(definition.beanClassName)
            if (TestsFilter.isTestClass(candidateClass) || candidateClass.isAnnotationPresent(Stateless::class.java)) {
                definition.scope = "prototype"
            } else if (candidateClass.isAnnotationPresent(Stateful::class.java)) {
                definition.scope = "test"
            }
            registry.registerBeanDefinition(candidateClass.name, definition)

            registerInterceptors(registry, candidateClass)
        }
    }

    private fun registerInterceptors(registry: BeanDefinitionRegistry, candidateClass: Class<*>) {
        candidateClass
                .methods
                .flatMap { it.interceptors() }
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