package com.gnefedev.javaee.junit

import com.gnefedev.javaee.model.TestResponse
import com.gnefedev.javaee.web.SessionIdHolder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import java.lang.reflect.Method

/**
 * Created by gerakln on 21.08.16.
 */
internal object TestServer {
    private val template : RestTemplate by lazy {
        val requestFactory = SimpleClientHttpRequestFactory()
        requestFactory.setConnectTimeout(500)
        requestFactory.setReadTimeout(5 * 1000)
        val restTemplate = RestTemplate()
        restTemplate.requestFactory = requestFactory
        restTemplate
    }

    fun getResponse(javaMethod: Method, type: String): TestResponse<*> {
        val className = javaMethod.declaringClass.name
        val methodName = javaMethod.name
        val uri = "http://localhost:8080/test/$type/$className/$methodName"
        val testResult: TestResponse<*>
        val headers = HttpHeaders()
        if (SessionIdHolder.sessionId != null) {
            headers.add("Cookie", "JSESSIONID=" + SessionIdHolder.sessionId)
        }
        try {
            testResult = TestServer.template
                    .exchange(
                            uri,
                            HttpMethod.GET,
                            HttpEntity(null, headers),
                            TestResponse::class.java
                    )
                    .body
        } catch(e: ResourceAccessException) {
            throw RuntimeException("Не найден продеплоеный тест по адресу $uri")
        }
        SessionIdHolder.sessionId = testResult.sessionId
        return testResult
    }
}