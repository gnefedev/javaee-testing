package com.gnefedev.javaee.offline

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.config.CustomScopeConfigurer
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.Configuration
import org.springframework.core.type.filter.AnnotationTypeFilter
import javax.ejb.Stateful
import javax.ejb.Stateless

/**
 * Created by gerakln on 03.09.16.
 */
@Configuration
internal open class OfflineConfig : BeanDefinitionRegistryPostProcessor {

    @Bean
    open fun testScope() : CustomScopeConfigurer {
        return CustomScopeConfigurer()
                .apply { addScope("test", TestScope) }
    }

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        val scanner = ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(TestsFilter())
        scanner.addIncludeFilter(AnnotationTypeFilter(Stateless::class.java))
        scanner.addIncludeFilter(AnnotationTypeFilter(Stateful::class.java))
        for (candidate in scanner.findCandidateComponents("com.gnefedev")) {
            val candidateClass = Class.forName(candidate.beanClassName)
            val builder = BeanDefinitionBuilder.genericBeanDefinition(candidateClass)
            if (candidateClass.isAnnotationPresent(Stateful::class.java)) {
                builder.setScope("test")
            } else if (TestsFilter.isTestClass(candidateClass)) {
                builder.setScope("prototype")
            }
            registry.registerBeanDefinition(
                    candidateClass.name,
                    builder.beanDefinition
            )
        }
    }

    override fun postProcessBeanFactory(configurableListableBeanFactory: ConfigurableListableBeanFactory) {

    }
}
