package com.gnefedev.jee.testing.offline.config

import com.atomikos.jms.AtomikosConnectionFactoryBean
import org.apache.activemq.ActiveMQXAConnectionFactory
import org.apache.activemq.command.ActiveMQQueue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.jms.ConnectionFactory
import javax.jms.Queue

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

    @Bean(name = arrayOf("jms/queue/requestQueue"))
    open fun requestQueue(): Queue = ActiveMQQueue("requestQueue")
    @Bean(name = arrayOf("jms/queue/responseQueue"))
    open fun responseQueue(): Queue = ActiveMQQueue("responseQueue")
}