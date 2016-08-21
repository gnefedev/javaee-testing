package com.gnefedev.javaee.junit

import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

/**
 * Created by gerakln on 21.08.16.
 */
internal object RestTemplateForTest {
    val template : RestTemplate by lazy {
        val requestFactory = SimpleClientHttpRequestFactory()
        requestFactory.setConnectTimeout(500)
        requestFactory.setReadTimeout(5 * 1000)
        val restTemplate = RestTemplate()
        restTemplate.requestFactory = requestFactory
        restTemplate
    }
}