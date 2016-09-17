package com.gnefedev.jee.testing.offline.config

import com.atomikos.icatch.jta.UserTransactionImp
import com.atomikos.icatch.jta.UserTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.jta.JtaTransactionManager
import javax.transaction.Status
import javax.transaction.UserTransaction

/**
 * Created by gerakln on 13.09.16.
 */
@Configuration
internal open class TransactionConfig {
    val TRANSACTION_TIMEOUT = 1000

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
                throw UnsupportedOperationException("not implemented")
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
}