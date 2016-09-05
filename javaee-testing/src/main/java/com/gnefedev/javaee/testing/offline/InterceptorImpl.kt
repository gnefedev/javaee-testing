package com.gnefedev.javaee.testing.offline

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import java.util.*

internal class InterceptorImpl : MethodInterceptor {
    var interceptors: MutableMap<Any, List<Any>> = WeakHashMap()

    override fun invoke(invocation: MethodInvocation): Any? {
        val result: Any?
        val target = invocation.`this`
        if (!interceptors.containsKey(target)) {
            interceptors[target] = invocation
                    .method
                    .interceptors()
                    .map { ContextHolder.context.getBean(it) }
                    .toList()
        }
        val interceptor = interceptors[target]!!.first()
        val aroundInvoke = interceptor.javaClass.aroundInvoke()
        val invocationContext = InvocationContextImpl(invocation)
        result = aroundInvoke.invoke(interceptor, invocationContext)
        return result
    }
}
