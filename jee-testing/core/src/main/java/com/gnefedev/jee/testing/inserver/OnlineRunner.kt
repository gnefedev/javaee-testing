package com.gnefedev.jee.testing.inserver

import com.gnefedev.jee.testing.junit.RunnerDelegate
import org.junit.runners.model.Statement

/**
 * Created by gerakln on 14.08.16.
 */
internal class OnlineRunner(private val klass: Class<*>) : RunnerDelegate(klass) {
    private fun sessionRegister(): EjbRegister {
        return OnlineContextHolder.getBean(EjbRegister::class.java)
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
}
