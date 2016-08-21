package com.gnefedev.javaee.junit

import com.gnefedev.javaee.model.TestResponse
import com.gnefedev.javaee.model.TestStatus
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import org.springframework.web.client.HttpClientErrorException

/**
 * Created by gerakln on 21.08.16.
 */
internal class WebMethodInvoker(val method: FrameworkMethod) : Statement() {
    override fun evaluate() {
        val className = method.method.declaringClass.name
        val methodName = method.name
        val uri = "http://localhost:8080/test/test/$className/$methodName"
        val response: TestResponse<*>?
        try {
            response = RestTemplateForTest.template
                           .getForEntity(uri, TestResponse::class.java)
                           .body
        } catch(e: HttpClientErrorException) {
            throw RuntimeException("Не найден продеплоеный тест по адресу $uri")
        }
        if (response.status == TestStatus.ERROR) {
            throw response.error!!
        }
    }
}