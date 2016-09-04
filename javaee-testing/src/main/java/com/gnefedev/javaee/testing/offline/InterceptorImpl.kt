package com.gnefedev.javaee.testing.offline

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation

/**
 * Created by gerakln on 04.09.16.
 */
internal class InterceptorImpl : MethodInterceptor {

    override fun invoke(invocation: MethodInvocation): Any {
        return invocation.proceed()
    }
}
