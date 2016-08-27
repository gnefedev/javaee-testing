package com.gnefedev.javaee.web

import com.gnefedev.javaee.model.TestResponse
import com.gnefedev.javaee.model.TestStatus
import org.junit.runner.JUnitCore
import org.junit.runner.Request
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession

@RestController
class TestController {
    @RequestMapping("/test/{testClass}/{methodName}", produces = arrayOf(MediaType.APPLICATION_XML_VALUE))
    fun runMethod(@PathVariable testClass: Class<*>, @PathVariable methodName: String, httpSession: HttpSession): TestResponse<*> {
        val junit = JUnitCore()
        val result = junit.run(Request.method(testClass, methodName))
        val response: TestResponse<Throwable>
        if (result.failureCount == 0) {
            response = TestResponse(TestStatus.SUCCESS, Throwable())
        } else {
            val failure = result.failures.first()
            response = TestResponse(TestStatus.ERROR, failure.exception.cause?:failure.exception)
        }
        response.sessionId = httpSession.id
        return response
    }
    @RequestMapping("/after/{testClass}/{methodName}", produces = arrayOf(MediaType.APPLICATION_XML_VALUE))
    fun runAfterClass(@PathVariable testClass: Class<*>, @PathVariable methodName: String, httpSession: HttpSession): TestResponse<*> {
        val afterMethod = testClass.getDeclaredMethod(methodName)
        val response: TestResponse<Throwable>
        try {
            afterMethod.invoke(null)
            response = TestResponse(TestStatus.SUCCESS, Throwable())
        } catch(e: Exception) {
            response = TestResponse(TestStatus.ERROR, e.cause!!)
        }
        response.sessionId = httpSession.id
        httpSession.invalidate()
        return response
    }
}
