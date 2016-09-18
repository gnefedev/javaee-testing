package com.gnefedev.jee.testing.offline.config

import com.atomikos.jms.AtomikosConnectionFactoryBean
import org.apache.activemq.ActiveMQXAConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.jms.ConnectionFactory

/**
 * Created by gerakln on 13.09.16.
 */
@Configuration
internal open class JmsConfig {
    val CLOSE_TIMEOUT = 10

    @Bean(initMethod = "init", destroyMethod = "close")
    open fun connectionFactory(): ConnectionFactory {
        val connectionFactory = ActiveMQXAConnectionFactory("vm://localhost?broker.persistent=false")
        connectionFactory.closeTimeout = CLOSE_TIMEOUT
        val atomikosConnectionFactory = AtomikosConnectionFactoryBean()
        atomikosConnectionFactory.uniqueResourceName = "xamqIn"
        atomikosConnectionFactory.setPoolSize(5)
        atomikosConnectionFactory.ignoreSessionTransactedFlag = false
        atomikosConnectionFactory.xaConnectionFactory = connectionFactory
        return atomikosConnectionFactory
    }
}