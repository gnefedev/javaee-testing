package com.gnefedev.javaee.web

import com.gnefedev.javaee.test.SampleTest
import org.junit.runner.JUnitCore
import org.junit.runner.Request
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @RequestMapping("/testAll")
    fun runAll(): String {
        val junit = JUnitCore()
        val result = junit.run(SampleTest::class.java)
        var response = ""
        result.failures.forEach { response += it.trace }
        if (result.failureCount == 0) {
            response += "run " + result.runCount
        }
        return response
    }

    @RequestMapping("/test/{testClass}/{methodName}")
    fun runMethod(@PathVariable testClass: Class<*>, @PathVariable methodName: String): String {
        val junit = JUnitCore()
        val result = junit.run(Request.method(testClass, methodName))
        var response = ""
        result.failures.forEach { response += it.trace }
        if (result.failureCount == 0) {
            response += "run " + result.runCount
        }
        return response
    }


}
