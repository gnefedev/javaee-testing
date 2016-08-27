package com.gnefedev.javaee.junit

import com.gnefedev.javaee.web.ContextHolder
import com.gnefedev.javaee.web.SessionRegister
import org.junit.AfterClass
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.InitializationError
import org.junit.runners.model.Statement
import javax.naming.InitialContext

/**
 * Created by gerakln on 14.08.16.
 */
class JavaeeTestRunner @Throws(InitializationError::class)
constructor(private val klass: Class<*>) : BlockJUnit4ClassRunner(klass) {
    fun sessionRegister(): SessionRegister {
        return ContextHolder.getBean(SessionRegister::class.java)
    }

    @Throws(Exception::class)
    override fun createTest(): Any {
        if (inServer) {
            return sessionRegister().getEjb(klass)
        } else {
            return super.createTest()
        }
    }

    override fun methodInvoker(method: FrameworkMethod?, test: Any?): Statement {
        if (inServer) {
            return super.methodInvoker(method, test)
        } else {
            return WebMethodInvoker(method!!)
        }
    }

    override fun methodBlock(method: FrameworkMethod?): Statement {
        val methodBlock = super.methodBlock(method)
        return methodBlock
    }

    override fun withBeforeClasses(statement: Statement?): Statement {
        if (inServer && sessionRegister().isFirstCall(klass)) {
            sessionRegister().registerCall(klass)
            return super.withBeforeClasses(statement)
        } else {
            return statement!!
        }
    }

    override fun withBefores(method: FrameworkMethod?, target: Any?, statement: Statement?): Statement {
        if (inServer) {
            return super.withBefores(method, target, statement)
        } else {
            return statement!!
        }
    }

    override fun withAfterClasses(statement: Statement?): Statement {
        if (inServer) {
            return statement!!
        } else {
            val afterMethods = testClass.getAnnotatedMethods(AfterClass::class.java)
            if (afterMethods.isEmpty()) {
                return statement!!
            } else {
                return WebAfterClass(statement!!, afterMethods.first().method)
            }
        }
    }

    override fun withAfters(method: FrameworkMethod?, target: Any?, statement: Statement?): Statement {
        if (inServer) {
            return super.withAfters(method, target, statement)
        } else {
            return statement!!
        }
    }

    companion object {
        private val initialContext: InitialContext? by lazy {
            val context: InitialContext?
            try {
                context = InitialContext()
                context.environment
            } catch(e: Exception) {
                context = null
            }
            context
        }

        private val inServer = initialContext != null
    }
}
