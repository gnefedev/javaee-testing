package com.gnefedev.javaee.testing.junit

import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement

/**
 * Created by gerakln on 27.08.16.
 */
abstract internal class RunnerDelegate(klass: Class<*>) : BlockJUnit4ClassRunner(klass) {
    @Throws(Exception::class)
    override public fun createTest(): Any {
        return super.createTest()
    }

    override public fun methodInvoker(method: FrameworkMethod?, test: Any?): Statement {
        return super.methodInvoker(method, test)
    }

    override public fun withBeforeClasses(statement: Statement?): Statement {
        return super.withBeforeClasses(statement)
    }

    override public fun withBefores(method: FrameworkMethod?, target: Any?, statement: Statement?): Statement {
        return super.withBefores(method, target, statement)
    }

    override public fun withAfterClasses(statement: Statement?): Statement {
        return super.withAfterClasses(statement)
    }

    override public fun withAfters(method: FrameworkMethod?, target: Any?, statement: Statement?): Statement {
        return super.withAfters(method, target, statement)
    }
}