package com.gnefedev.javaee.model

/**
 * Created by gerakln on 21.08.16.
 */
data class TestResponse<out T : Throwable>(val status: TestStatus = TestStatus.SUCCESS, val error: T? = null) {
}