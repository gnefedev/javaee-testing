package com.gnefedev.javaee.web

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * Created by gerakln on 21.08.16.
 */
@Component
object ContextHolder : ApplicationContextAware {
    lateinit var context: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext?) {
        context = applicationContext!!
    }

    fun <T> getBean(clazz: Class<T>): T {
        return context.getBean(clazz)
    }
}
