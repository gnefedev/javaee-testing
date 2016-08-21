package com.gnefedev.javaee.junit

import com.gnefedev.javaee.model.TestResponse
import com.gnefedev.javaee.model.TestStatus
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement

/**
 * Created by gerakln on 21.08.16.
 */
internal class WebMethodInvoker(val method: FrameworkMethod) : Statement() {
    override fun evaluate() {
        val className = method.method.declaringClass.name
        val methodName = method.name
        val uri = "http://localhost:8080/test/test/$className/$methodName"
        val response = RestTemplateForTest.template
                .getForEntity(uri, TestResponse::class.java)
                .body
        if (response.status == TestStatus.ERROR) {
            throw response.error!!
        }
    }
}