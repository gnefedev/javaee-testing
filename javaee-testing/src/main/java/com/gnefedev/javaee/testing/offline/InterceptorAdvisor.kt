package com.gnefedev.javaee.testing.offline

import org.aopalliance.aop.Advice
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor

import java.lang.reflect.Method
import javax.interceptor.Interceptors

/**
 * Created by gerakln on 04.09.16.
 */
internal class InterceptorAdvisor(advice: Advice) : StaticMethodMatcherPointcutAdvisor(advice) {
    override fun matches(method: Method, targetClass: Class<*>): Boolean {
        return targetClass.isAnnotationPresent(Interceptors::class.java)
                || method.isAnnotationPresent(Interceptors::class.java)
    }
}
