package com.gnefedev.javaee.junit

import com.gnefedev.javaee.web.ContextHolder
import com.gnefedev.javaee.web.EjbRegister
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement

/**
 * Created by gerakln on 14.08.16.
 */
internal class OnlineRunner(private val klass: Class<*>) : RunnerDelegate(klass) {
    private fun sessionRegister(): EjbRegister {
        return ContextHolder.getBean(EjbRegister::class.java)
    }

    @Throws(Exception::class)
    override fun createTest(): Any {
        return sessionRegister().getEjb(klass)
    }

    override fun withBeforeClasses(statement: Statement?): Statement {
        if (sessionRegister().isFirstCall(klass)) {
            sessionRegister().registerCall(klass)
            return super.withBeforeClasses(statement)
        } else {
            return statement!!
        }
    }

    override fun withAfterClasses(statement: Statement?): Statement {
        return statement!!
    }

    override fun withAfters(method: FrameworkMethod?, target: Any?, statement: Statement?): Statement {
        return super.withAfters(method, target, statement)
    }
}
