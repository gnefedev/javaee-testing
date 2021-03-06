package com.gnefedev.jee.testing.against.server

import com.gnefedev.jee.testing.model.TestStatus
import org.junit.runners.model.MultipleFailureException
import org.junit.runners.model.Statement
import java.lang.reflect.Method

/**
 * Created by gerakln on 22.08.16.
 */
internal class WebAfterClass(val base: Statement, val afterClassMethods: List<Method>) : Statement() {
    override fun evaluate() {
        val errors: MutableList<Throwable> = mutableListOf()
        try {
            base.evaluate()
        } catch (e: Throwable) {
            errors.add(e)
        } finally {
            for (method in afterClassMethods) {
                val testResult = TestServer.getResponse(method, "after")
                if (testResult.status == TestStatus.ERROR) {
                    errors.add(testResult.error)
                }
            }
            TestServer.sessionId = null
        }
        MultipleFailureException.assertEmpty(errors)
    }
}