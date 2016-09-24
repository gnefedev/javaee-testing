package com.gnefedev.jee.testing.offline.interceptors

import javax.interceptor.InvocationContext

/**
 * Created by gerakln on 05.09.16.
 */
internal class InvocationContextEE(val invoker: InterceptorInvoker) : InvocationContext by invoker.context {
    override fun proceed(): Any? = invoker.invoke()
}