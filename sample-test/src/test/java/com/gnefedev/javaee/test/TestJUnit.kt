package com.gnefedev.javaee.test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.Description
import org.junit.runner.JUnitCore
import org.junit.runner.Request
import org.junit.runner.manipulation.Filter

/**
 * Created by gerakln on 14.08.16.
 */
class TestJUnit {
    @Test
    fun testInject() {
        val junit = JUnitCore()
        val result = junit.run(TestInject::class.java)
        result.failures.forEach {
            println(it.trace)
        }
        assertEquals(0, result.failureCount.toLong())
    }

    @Test
    fun testErrors() {
        val junit = JUnitCore()
        val request = Request
                .aClass(TestErrors::class.java)
                .filterWith(object : Filter() {
                    override fun shouldRun(description: Description): Boolean {
                        return description.methodName != "_ifThisCalledTestWillNotFail"
                    }

                    override fun describe() = "For throws exceptions only with this filter"
                })
        val result = junit.run(request)
        assertEquals(2, result.failureCount.toLong())
        val assertFail = result.failures
                .filter { it.description.methodName == "assertFail" }
                .last()
        assertEquals("assertFail message", assertFail.message)
        assertTrue(assertFail.exception.javaClass.name, assertFail.exception is AssertionError)

        val npe = result.failures
                .filter { it.description.methodName == "npe" }
                .last()
        assertTrue(npe.exception.javaClass.name, npe.exception is NullPointerException)
    }
}
