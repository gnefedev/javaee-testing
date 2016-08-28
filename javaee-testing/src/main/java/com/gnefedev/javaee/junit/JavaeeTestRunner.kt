package com.gnefedev.javaee.junit

import com.gnefedev.javaee.against.server.AgainstServerRunner
import com.gnefedev.javaee.online.OnlineRunner
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import javax.naming.InitialContext

/**
 * Created by gerakln on 14.08.16.
 */
class JavaeeTestRunner (klass: Class<*>) : BlockJUnit4ClassRunner(klass) {
    private val delegate: RunnerDelegate

    init {
        if (inServer) {
            delegate = OnlineRunner(klass)
        } else {
            delegate = AgainstServerRunner(klass)
        }
    }

    @Throws(Exception::class)
    override fun createTest(): Any {
        return delegate.createTest()
    }

    override fun methodInvoker(method: FrameworkMethod?, test: Any?): Statement {
        return delegate.methodInvoker(method, test)
    }

    override fun withBeforeClasses(statement: Statement?): Statement {
        return delegate.withBeforeClasses(statement)
    }

    override fun withBefores(method: FrameworkMethod?, target: Any?, statement: Statement?): Statement {
        return delegate.withBefores(method, target, statement)
    }

    override fun withAfterClasses(statement: Statement?): Statement {
        return delegate.withAfterClasses(statement)
    }

    override fun withAfters(method: FrameworkMethod?, target: Any?, statement: Statement?): Statement {
        return delegate.withAfters(method, target, statement)
    }

    companion object {
        private val inServer: Boolean by lazy {
            try {
                InitialContext().environment
            } catch(e: Exception) {
                return@lazy false
            }
            return@lazy true
        }
    }
}
