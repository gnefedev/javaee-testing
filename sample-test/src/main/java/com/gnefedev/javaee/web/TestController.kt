package com.gnefedev.javaee.web

import com.gnefedev.javaee.model.TestCase
import com.gnefedev.javaee.test.SampleTest
import org.junit.runner.JUnitCore
import org.junit.runner.Request
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @RequestMapping("/testAll")
    fun runAll(): String {
        val junit = JUnitCore()
//        val result = junit.run(Request.method(SampleTest::class.java, "online"))
        val result = junit.run(SampleTest::class.java)
        var response = ""
        result.failures.forEach { response += it.trace }
        if (result.failureCount == 0) {
            response += "run " + result.runCount
        }
        return response
    }

    @RequestMapping("/test")
    fun runMethod(@RequestBody testCase: TestCase): String {
        val junit = JUnitCore()
        val result = junit.run(Request.method(testCase.clazz, testCase.method))
        var response = ""
        result.failures.forEach { response += it.trace }
        if (result.failureCount == 0) {
            response += "run " + result.runCount
        }
        return response
    }


}
