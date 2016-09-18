package com.gnefedev.jee.testing.offline.scanner

import com.gnefedev.jee.testing.Config
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import javax.ejb.MessageDriven
import javax.ejb.Stateful
import javax.ejb.Stateless
import javax.validation.ValidationException

/**
 * Created by gerakln on 04.09.16.
 */
internal object JeeScanner : BeanDefinitionRegistryPostProcessor {
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
        scanner
                .findCandidateComponents(packageToScan)
                .map { BeanRegistrant(registry, it) }
                .forEach (BeanRegistrant::register)
    }

    override fun postProcessBeanFactory(configurableListableBeanFactory: ConfigurableListableBeanFactory) {
    }
}