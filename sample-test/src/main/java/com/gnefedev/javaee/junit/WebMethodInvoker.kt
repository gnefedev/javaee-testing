package com.gnefedev.javaee.junit

import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import org.springframework.web.client.RestTemplate

/**
 * Created by gerakln on 21.08.16.
 */
internal class WebMethodInvoker(val method: FrameworkMethod) : Statement() {
    override fun evaluate() {
        val className = method.method.declaringClass.name
        val methodName = method.name
        val uri = "http://localhost:8080/test/test/$className/$methodName"
        RestTemplate().getForEntity(uri, String::class.java)
    }
}