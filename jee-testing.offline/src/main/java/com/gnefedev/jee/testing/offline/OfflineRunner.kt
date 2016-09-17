package com.gnefedev.jee.testing.offline

import com.gnefedev.jee.testing.junit.RunnerDelegate
import org.junit.runners.model.Statement

/**
 * Created by gerakln on 03.09.16.
 */
class OfflineRunner(private val klass: Class<*>) : RunnerDelegate(klass) {
    override fun createTest(): Any {
        return OfflineContextHolder.context.getBean(klass)
    }

    override fun withAfterClasses(statement: Statement?): Statement {
        return OfflineAfterClass(super.withAfterClasses(statement))
    }
}