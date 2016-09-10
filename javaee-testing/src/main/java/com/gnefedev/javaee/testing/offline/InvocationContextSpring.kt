package com.gnefedev.javaee.testing.offline

import org.aopalliance.intercept.MethodInvocation
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import javax.interceptor.InvocationContext

/**
 * Created by gerakln on 05.09.16.
 */
internal class InvocationContextSpring(val invocation: MethodInvocation) : InvocationContext {
    override fun proceed(): Any? = invocation.proceed()

    override fun getTarget(): Any = invocation.`this`

    override fun getConstructor(): Constructor<*> {
        return invocation.`this`.javaClass.constructors.first()
    }

    override fun getMethod(): Method = invocation.method

    override fun setParameters(params: Array<out Any>?) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTimer(): Any {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getParameters(): Array<out Any> = invocation.arguments

    override fun getContextData(): MutableMap<String, Any>? {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}