package com.gnefedev.javaee.junit

import com.gnefedev.javaee.model.TestStatus
import org.junit.internal.runners.model.MultipleFailureException
import org.junit.runners.model.Statement
import java.lang.reflect.Method

/**
 * Created by gerakln on 22.08.16.
 */
internal class WebAfterClass(val base: Statement, val afterClass: Method) : Statement() {
    override fun evaluate() {
        val errors: MutableList<Throwable> = mutableListOf()
        try {
            base.evaluate()
        } catch (e: Throwable) {
            errors.add(e)
        } finally {
            val testResult = TestServer.getResponse(afterClass, "after")
            if (testResult.status == TestStatus.ERROR) {
                errors.add(testResult.error)
            }
        }
        MultipleFailureException.assertEmpty(errors)
    }
}