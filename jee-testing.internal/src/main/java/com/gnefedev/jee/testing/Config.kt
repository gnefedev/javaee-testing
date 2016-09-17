package com.gnefedev.jee.testing

import java.util.*

/**
 * Created by gerakln on 03.09.16.
 */
object Config {
    private val properties: Properties by lazy {
        val prop = Properties()
        try {
            prop.load(Config::class.java.classLoader.getResourceAsStream("javaee-testing.properties"))
        } catch (ignored: Exception) {
        }
        return@lazy prop
    }

    val host: String = properties.getProperty("host", "localhost")
    val packageToScan: String = properties.getProperty("packageToScan")
    val port: Int = properties.getProperty("port", "8080").toInt()
    val contextRoot: String = properties.getProperty("context-root", "javaee-testing")
    val testMode: String = properties.getProperty("test-mode", "auto").toUpperCase()
}