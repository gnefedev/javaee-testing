package com.gnefedev.javaee.testing.offline

import com.atomikos.icatch.jta.UserTransactionImp
import com.atomikos.icatch.jta.UserTransactionManager
import com.atomikos.jdbc.AtomikosDataSourceBean
import org.hsqldb.jdbc.pool.JDBCXADataSource
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
import org.springframework.beans.factory.config.CustomScopeConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.jta.JtaTransactionManager
import javax.naming.Context
import javax.naming.InitialContext
import javax.transaction.Status
import javax.transaction.UserTransaction

/**
 * Created by gerakln on 03.09.16.
 */
@Configuration
internal open class OfflineConfig {
    val TRANSACTION_TIMEOUT = 1000

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

    @Bean
    open fun transactionManager(): PlatformTransactionManager {
        val transactionManager = UserTransactionManager()
        transactionManager.forceShutdown = false
        val userTransaction = UserTransactionImp()
        userTransaction.setTransactionTimeout(TRANSACTION_TIMEOUT)
        return JtaTransactionManager(userTransaction, transactionManager)
    }

    @Bean
    open fun userTransaction(): UserTransaction {
        return object: UserTransaction {
            override fun getStatus() = Status.STATUS_UNKNOWN

            override fun rollback() {
                transactionManager().rollback(transactionManager().getTransaction(null))
            }

            override fun setTransactionTimeout(seconds: Int) {
                throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun commit() {
                transactionManager().commit(transactionManager().getTransaction(null))
            }

            override fun begin() {
                transactionManager().getTransaction(null)
            }

            override fun setRollbackOnly() {
                transactionManager().getTransaction(null).setRollbackOnly()
            }

        }
    }


    @Bean
    open fun entityManager(): LocalContainerEntityManagerFactoryBean {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.naming.java.javaURLContextFactory")
        System.setProperty(Context.URL_PKG_PREFIXES,
                "org.apache.naming")
        val ic = InitialContext()

        ic.createSubcontext("java:")
        ic.createSubcontext("java:/comp")
        ic.createSubcontext("java:/comp/env")
        ic.createSubcontext("java:/jdbc")

        // Construct DataSource
        val atomikosDataSourceBean = AtomikosDataSourceBean()
        atomikosDataSourceBean.uniqueResourceName = "zoo"

        val xaDataSource = JDBCXADataSource()
        xaDataSource.setPassword("sa")
        xaDataSource.user = ""
        xaDataSource.url = "jdbc:hsqldb:mem:unit-testing-jpa"

        atomikosDataSourceBean.xaDataSource = xaDataSource
        atomikosDataSourceBean.init()

        ic.bind("java:/jdbc/zoo", atomikosDataSourceBean)

        val managerFactoryBean = LocalContainerEntityManagerFactoryBean()
        return managerFactoryBean
    }

}
