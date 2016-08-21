package com.gnefedev.javaee.web

import com.gnefedev.javaee.model.TestResponse
import com.gnefedev.javaee.test.TestInject
import org.junit.runner.JUnitCore
import org.junit.runner.Request
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @RequestMapping("/testAll")
    fun runAll(): String {
        val junit = JUnitCore()
        val result = junit.run(TestInject::class.java)
        var response = ""
        result.failures.forEach { response += it.trace }
        if (result.failureCount == 0) {
            response += "run " + result.runCount
        }
        return response
    }

    @RequestMapping("/test/{testClass}/{methodName}", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun runMethod(@PathVariable testClass: Class<*>, @PathVariable methodName: String): TestResponse {
        val junit = JUnitCore()
        val result = junit.run(Request.method(testClass, methodName))
        if (result.failureCount == 0) {
            return TestResponse()
        } else {
            val failure = result.failures.first()
            return TestResponse(true, failure.exception.cause!!.message?:"")
        }
    }


}
