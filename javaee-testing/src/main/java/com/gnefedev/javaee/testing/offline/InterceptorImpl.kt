package com.gnefedev.javaee.testing.offline

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import java.util.*
import javax.interceptor.InvocationContext

internal class InterceptorImpl : MethodInterceptor {
    var interceptorsMap: MutableMap<Any, List<Any>> = WeakHashMap()

    override fun invoke(invocation: MethodInvocation): Any? {
        val target = invocation.`this`
        val interceptors = getInterceptors(invocation, target)
        if (interceptors.isEmpty()) {
            return invocation.proceed()
        }
        var interceptorInvoker: InterceptorInvoker? = null
        for (interceptor in interceptors) {
            val aroundInvoke = interceptor.javaClass.aroundInvoke()
            val invocationContext: InvocationContext
            if (interceptorInvoker == null) {
                invocationContext = InvocationContextSpring(invocation)
            } else {
                invocationContext = InvocationContextEE(interceptorInvoker)
            }
            interceptorInvoker = InterceptorInvoker(invocationContext, aroundInvoke, interceptor)
        }
        return interceptorInvoker!!.invoke()
    }

    private fun getInterceptors(invocation: MethodInvocation, target: Any): List<Any> {
        val interceptors = interceptorsMap.getOrPut(target) {
            invocation
                    .method
                    .getInterceptors()
                    .map { ContextHolder.context.getBean(it) }
                    .toList()
        }
        return interceptors
    }
}
