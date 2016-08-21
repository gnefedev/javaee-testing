package com.gnefedev.javaee.test

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.JUnitCore

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
        val result = junit.run(TestErrors::class.java)
        result.failures.forEach {
            println(it.trace)
        }
        assertEquals(1, result.failureCount.toLong())
        val assertFail = result.failures
                .filter { it.description.methodName == "assertFail" }
                .last()
        assertEquals("assertFail message", assertFail.message)
    }
}
