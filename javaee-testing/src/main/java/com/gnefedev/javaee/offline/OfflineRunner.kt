package com.gnefedev.javaee.offline

import com.gnefedev.javaee.against.server.OfflineAfterClass
import com.gnefedev.javaee.junit.RunnerDelegate
import org.junit.runners.model.Statement

/**
 * Created by gerakln on 03.09.16.
 */
class OfflineRunner(private val klass: Class<*>) : RunnerDelegate(klass) {
    override fun createTest(): Any {
        return ContextHolder.context.getBean(klass)
    }

    override fun withAfterClasses(statement: Statement?): Statement {
        return OfflineAfterClass(super.withAfterClasses(statement))
    }
}