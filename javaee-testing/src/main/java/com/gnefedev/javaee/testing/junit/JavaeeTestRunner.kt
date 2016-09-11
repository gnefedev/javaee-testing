package com.gnefedev.javaee.testing.junit

import com.gnefedev.javaee.testing.against.server.AgainstServerRunner
import com.gnefedev.javaee.testing.model.TestMode.*
import com.gnefedev.javaee.testing.offline.OfflineRunner
import com.gnefedev.javaee.testing.inserver.OnlineRunner
import com.gnefedev.javaee.testing.Config
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement

/**
 * Created by gerakln on 14.08.16.
 */
class JavaeeTestRunner (klass: Class<*>) : BlockJUnit4ClassRunner(klass) {
    private val delegate: RunnerDelegate

    init {
        when (Config.testMode) {
            OFFLINE -> delegate = OfflineRunner(klass)
            IN_SERVER -> delegate = OnlineRunner(klass)
            AGAINST_SERVER -> delegate = AgainstServerRunner(klass)
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
}
