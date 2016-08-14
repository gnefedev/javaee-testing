package com.gnefedev.javaee.junit

import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.InitializationError
import javax.naming.InitialContext

/**
 * Created by gerakln on 14.08.16.
 */
class JavaeeTestRunner @Throws(InitializationError::class)
constructor(private val klass: Class<*>) : BlockJUnit4ClassRunner(klass) {

    @Throws(Exception::class)
    override fun createTest(): Any {
        if (initialContext != null) {
            return initialContext!!.lookup("java:module/${klass.simpleName}")
        } else {
            return super.createTest()
        }
    }

    companion object {

        private val initialContext: InitialContext? by lazy {
            val context: InitialContext?
            try {
                context = InitialContext()
                context.environment
            } catch(e: Exception) {
                context = null
            }
            context
        }
    }
}
