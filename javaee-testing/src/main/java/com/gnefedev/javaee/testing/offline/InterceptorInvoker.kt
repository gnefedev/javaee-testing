package com.gnefedev.javaee.testing.offline

import java.lang.reflect.Method
import javax.interceptor.AroundInvoke
import javax.interceptor.InvocationContext

/**
 * Created by gerakln on 10.09.16.
 */
internal class InterceptorInvoker(
        val context: InvocationContext,
        private val interceptor: Any
) {
    private val method: Method = interceptor
            .javaClass
            .methods
            .filter { it.isAnnotationPresent(AroundInvoke::class.java) }
            .first()

    fun invoke(): Any? = method.invoke(interceptor, context)
}