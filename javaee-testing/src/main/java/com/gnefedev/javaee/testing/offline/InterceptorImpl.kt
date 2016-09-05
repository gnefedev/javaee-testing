package com.gnefedev.javaee.testing.offline

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation

internal class InterceptorImpl : MethodInterceptor {
    var interceptors: List<Any> = emptyList()

    override fun invoke(invocation: MethodInvocation): Any? {
        val result: Any?
        if (interceptors.isEmpty()) {
            interceptors = invocation
                    .method
                    .interceptors()
                    .map { ContextHolder.context.getBean(it) }
                    .toList()
        }
        val interceptor = interceptors.first()
        val aroundInvoke = interceptor.javaClass.aroundInvoke()
        val invocationContext = InvocationContextImpl(invocation)
        result = aroundInvoke.invoke(interceptor, invocationContext)
        return result
    }
}
