package com.gnefedev.javaee.testing.offline

import java.lang.reflect.Method
import javax.interceptor.AroundInvoke
import javax.interceptor.Interceptors

/**
 * Created by gerakln on 04.09.16.
 */
fun Method.interceptors(): List<Class<out Any>> {
    if (!declaringClass.isAnnotationPresent(Interceptors::class.java)) {
        return emptyList()
    }
    val interceptors = declaringClass.getAnnotation(Interceptors::class.java).value
    return interceptors.map { it.java }
}

fun Class<*>.aroundInvoke(): Method {
    return methods
            .filter { it.isAnnotationPresent(AroundInvoke::class.java) }
            .first()
}