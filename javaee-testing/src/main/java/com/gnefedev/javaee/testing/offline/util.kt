package com.gnefedev.javaee.testing.offline

import java.lang.reflect.Method
import javax.interceptor.Interceptors
import kotlin.reflect.KClass

/**
 * Created by gerakln on 04.09.16.
 */
fun Method.getInterceptors(): List<Class<out Any>> {
    val classInterceptors: Array<KClass<*>>
    if (declaringClass.isAnnotationPresent(Interceptors::class.java)) {
        classInterceptors = declaringClass.getAnnotation(Interceptors::class.java).value
    } else {
        classInterceptors = emptyArray()
    }
    val methodInterceptors: Array<KClass<*>>
    if (isAnnotationPresent(Interceptors::class.java)) {
        methodInterceptors = getAnnotation(Interceptors::class.java).value
    } else {
        methodInterceptors = emptyArray()
    }
    return classInterceptors.map { it.java } + methodInterceptors.map { it.java }
}