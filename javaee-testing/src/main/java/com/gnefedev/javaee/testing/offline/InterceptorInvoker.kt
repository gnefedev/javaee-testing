package com.gnefedev.javaee.testing.offline

import java.lang.reflect.Method
import javax.interceptor.InvocationContext

/**
 * Created by gerakln on 10.09.16.
 */
internal class InterceptorInvoker(
        val context: InvocationContext,
        private val method: Method,
        private val interceptor: Any
) {
    fun invoke(): Any? = method.invoke(interceptor, context)
}