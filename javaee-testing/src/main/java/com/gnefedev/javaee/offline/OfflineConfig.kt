package com.gnefedev.javaee.offline

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.Configuration
import org.springframework.core.type.filter.AnnotationTypeFilter
import javax.ejb.Stateful
import javax.ejb.Stateless

/**
 * Created by gerakln on 03.09.16.
 */
@Configuration
open class OfflineConfig : BeanDefinitionRegistryPostProcessor {

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        val scanner = ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(TestsFilter())
        scanner.addIncludeFilter(AnnotationTypeFilter(Stateless::class.java))
        scanner.addIncludeFilter(AnnotationTypeFilter(Stateful::class.java))
        for (candidate in scanner.findCandidateComponents("com.gnefedev")) {
            val definition = BeanDefinitionBuilder
                    .genericBeanDefinition(candidate.beanClassName)
                    .beanDefinition
            registry.registerBeanDefinition(candidate.beanClassName, definition)
        }
    }

    override fun postProcessBeanFactory(configurableListableBeanFactory: ConfigurableListableBeanFactory) {

    }
}
