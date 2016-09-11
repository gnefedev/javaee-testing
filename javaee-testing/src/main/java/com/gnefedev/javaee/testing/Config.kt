package com.gnefedev.javaee.testing

import com.gnefedev.javaee.testing.against.server.TestServer
import com.gnefedev.javaee.testing.inserver.WebInit
import com.gnefedev.javaee.testing.model.TestMode
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
    val port: Int = properties.getProperty("port", "8080").toInt()
    val contextRoot: String = properties.getProperty("context-root", "javaee-testing")

    val testMode: TestMode by lazy {
        val result = testMode()
        //TODO to log or remove
        println(result)
        return@lazy result
    }

    private fun testMode(): TestMode {
        if (WebInit.isStarted) {
            return TestMode.IN_SERVER
        } else {
            val fromFile = properties.getProperty("test-mode", "auto").toUpperCase()
            if (fromFile != "AUTO") {
                return TestMode.valueOf(fromFile)
            } else if (TestServer.ping()) {
                return TestMode.AGAINST_SERVER
            } else {
                return TestMode.OFFLINE
            }
        }
    }
}