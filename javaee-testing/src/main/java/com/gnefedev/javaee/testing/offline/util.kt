package com.gnefedev.javaee.testing.offline

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.lang.reflect.Method
import javax.interceptor.Interceptors
import javax.naming.InitialContext
import javax.naming.NamingException
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

fun InitialContext.fullBind(jndiName: String, subject: Any) {
    val nameParts = jndiName.split("/")
    for (i in 0..nameParts.size - 2) {
        val namePart = nameParts.slice(0..i).joinToString("/")
        try {
            lookup(namePart)
        } catch(e: NamingException) {
            createSubcontext(namePart)
        }
    }
    try {
        lookup(jndiName)
    } catch(e: NamingException) {
        bind(jndiName, subject)
    }
}

fun NodeList.toList(): List<Node> {
    val nodes = mutableListOf<Node>()
    for (i in 0..length - 1) {
        nodes.add(item(i))
    }
    return nodes
}