package com.gnefedev.javaee.against.server

import com.gnefedev.javaee.junit.RunnerDelegate
import org.junit.AfterClass
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement

/**
 * Created by gerakln on 14.08.16.
 */
class AgainstServerRunner(klass: Class<*>) : RunnerDelegate(klass) {
    override fun methodInvoker(method: FrameworkMethod?, test: Any?): Statement {
        return WebMethodInvoker(method!!)
    }

    override fun withBeforeClasses(statement: Statement?): Statement {
        return statement!!
    }

    override fun withBefores(method: FrameworkMethod?, target: Any?, statement: Statement?): Statement {
        return statement!!
    }

    override fun withAfterClasses(statement: Statement?): Statement {
        val afterMethods = testClass.getAnnotatedMethods(AfterClass::class.java)
        return WebAfterClass(statement!!, afterMethods.map { it.method })
    }

    override fun withAfters(method: FrameworkMethod?, target: Any?, statement: Statement?): Statement {
        return statement!!
    }
}
