package com.gnefedev.javaee.testing.against.server

import com.gnefedev.javaee.testing.model.TestResponse
import com.gnefedev.javaee.testing.util.Config
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.lang.reflect.Method

/**
 * Created by gerakln on 21.08.16.
 */
object TestServer {
    var sessionId: String? = null

    private val baseUrl = "http://${Config.host}:${Config.port}/${Config.contextRoot}"

    private fun addSessionHeader(headers: HttpHeaders) {
        if (sessionId != null) {
            headers.add("Cookie", "JSESSIONID=" + sessionId)
        }
    }

    private val template: RestTemplate by lazy {
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
        val uri = "$baseUrl/$type/$className/$methodName"
        val testResult: TestResponse<*>
        val headers = HttpHeaders()
        addSessionHeader(headers)
        try {
            testResult = template
                    .exchange(
                            uri,
                            HttpMethod.GET,
                            HttpEntity(null, headers),
                            TestResponse::class.java
                    )
                    .body
            sessionId = testResult.sessionId
        } catch(e: RestClientException) {
            throw RuntimeException("Не найден продеплоеный тест по адресу $uri")
        }
        return testResult
    }

    fun ping(): Boolean {
        try {
            template.getForEntity("$baseUrl/pong", String::class.java)
            return true
        } catch(e: RestClientException) {
            return false
        }
    }
}