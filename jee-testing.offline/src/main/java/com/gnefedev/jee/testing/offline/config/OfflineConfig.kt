package com.gnefedev.jee.testing.offline.config

import com.gnefedev.jee.testing.offline.InterceptorAdvisor
import com.gnefedev.jee.testing.offline.InterceptorImpl
import com.gnefedev.jee.testing.offline.JeeScanner
import com.gnefedev.jee.testing.offline.TestScope
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
import org.springframework.beans.factory.config.CustomScopeConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import javax.naming.Context

/**
 * Created by gerakln on 03.09.16.
 */
@Configuration
@Import(
        PersistenceConfig::class,
        TransactionConfig::class
)
internal open class OfflineConfig {
    init {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory")
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming")
    }

    @Bean
    open fun testScope() = CustomScopeConfigurer().apply { addScope("test", TestScope) }

    @Bean
    open fun proxyCreator() = DefaultAdvisorAutoProxyCreator()

    @Bean
    open fun interceptor() = InterceptorImpl()

    @Bean
    open fun interceptorAdvisor() = InterceptorAdvisor(interceptor())

    @Bean
    open fun jeeScanner() = JeeScanner()
}
