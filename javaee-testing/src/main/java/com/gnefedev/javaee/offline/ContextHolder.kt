package com.gnefedev.javaee.offline

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * Created by gerakln on 03.09.16.
 */
object ContextHolder {
    val context: ApplicationContext by lazy {
        val applicationContext = AnnotationConfigApplicationContext()
        applicationContext.register(OfflineConfig::class.java)
        applicationContext.refresh()
        applicationContext.start()
        return@lazy applicationContext
    }
}