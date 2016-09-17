package com.gnefedev.jee.testing.offline

import org.junit.runners.model.MultipleFailureException
import org.junit.runners.model.Statement

/**
 * Created by gerakln on 22.08.16.
 */
internal class OfflineAfterClass(val base: Statement) : Statement() {
    override fun evaluate() {
        val errors: MutableList<Throwable> = mutableListOf()
        try {
            base.evaluate()
        } catch (e: Throwable) {
            errors.add(e)
        } finally {
            TestScope.clear()
        }
        MultipleFailureException.assertEmpty(errors)
    }
}