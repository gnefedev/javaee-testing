package com.gnefedev.javaee.testing.offline

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
import org.springframework.beans.factory.config.CustomScopeConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by gerakln on 03.09.16.
 */
@Configuration
internal open class OfflineConfig {

    @Bean
    open fun testScope() : CustomScopeConfigurer {
        return CustomScopeConfigurer()
                .apply { addScope("test", TestScope) }
    }

    @Bean
    open fun proxyCreator() = DefaultAdvisorAutoProxyCreator()

    @Bean
    open fun interceptor() = InterceptorImpl()

    @Bean
    open fun interceptorAdvisor() = InterceptorAdvisor(interceptor())

    @Bean
    open fun jeeScanner() = JeeScanner()

}
