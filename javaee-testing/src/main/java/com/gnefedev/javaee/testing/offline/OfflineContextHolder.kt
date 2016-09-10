package com.gnefedev.javaee.testing.offline

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * Created by gerakln on 03.09.16.
 */
internal object OfflineContextHolder {
    val context: ApplicationContext by lazy {
        val applicationContext = AnnotationConfigApplicationContext()
        applicationContext.register(OfflineConfig::class.java)
        applicationContext.refresh()
        applicationContext.start()
        return@lazy applicationContext
    }
}