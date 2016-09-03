package com.gnefedev.javaee.offline

import com.gnefedev.javaee.junit.RunnerDelegate

/**
 * Created by gerakln on 03.09.16.
 */
class OfflineRunner(private val klass: Class<*>) : RunnerDelegate(klass) {
    override fun createTest(): Any {
        throw NotImplementedError()
    }
}