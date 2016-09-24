package com.gnefedev.jee.testing.junit

import com.gnefedev.jee.testing.Config
import com.gnefedev.jee.testing.against.server.AgainstServerRunner
import com.gnefedev.jee.testing.against.server.TestServer
import com.gnefedev.jee.testing.inserver.OnlineRunner
import com.gnefedev.jee.testing.inserver.WebInit
import com.gnefedev.jee.testing.model.TestMode
import com.gnefedev.jee.testing.model.TestMode.*
import com.gnefedev.jee.testing.offline.OfflineRunner
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement

/**
 * Created by gerakln on 14.08.16.
 */
class JavaeeTestRunner (klass: Class<*>) : BlockJUnit4ClassRunner(klass) {
    private val delegate: RunnerDelegate

    init {
        when (testMode) {
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

    companion object {
        val testMode: TestMode by lazy {
            val result = testMode()
            //TODO to log or remove
            println(result)
            return@lazy result
        }

        private fun testMode(): TestMode {
            if (WebInit.isStarted) {
                return TestMode.IN_SERVER
            } else {
                val fromFile = Config.testMode
                if (fromFile != "AUTO") {
                    return TestMode.valueOf(fromFile)
                } else if (TestServer.ping()) {
                    return TestMode.AGAINST_SERVER
                } else {
                    return TestMode.OFFLINE
                }
            }
        }
    }
}
