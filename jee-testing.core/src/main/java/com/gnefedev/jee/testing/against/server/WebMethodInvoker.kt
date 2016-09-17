package com.gnefedev.jee.testing.against.server

import com.gnefedev.jee.testing.model.TestStatus
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement

/**
 * Created by gerakln on 21.08.16.
 */
internal class WebMethodInvoker(val method: FrameworkMethod) : Statement() {
    override fun evaluate() {
        val testResult = TestServer.getResponse(method.method, "test")
        if (testResult.status == TestStatus.ERROR) {
            throw testResult.error!!
        }
    }
}