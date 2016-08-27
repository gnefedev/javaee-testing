package com.gnefedev.javaee.web

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import javax.naming.InitialContext

/**
 * Created by gerakln on 21.08.16.
 */
@Component
@Scope("session")
class EjbRegister {
    private val ejbs: MutableMap<Class<*>, Any> = mutableMapOf()
    private val wasCalled: MutableMap<Class<*>, Boolean> = mutableMapOf()

    fun getEjb(klass: Class<*>): Any {
        if (!ejbs.containsKey(klass)) {
            ejbs[klass] = initialContext.lookup("java:module/${klass.simpleName}")
        }
        return ejbs[klass]!!
    }

    fun isFirstCall(klass: Class<*>): Boolean {
        val thisClassWasCalled = wasCalled[klass] ?: false
        return !thisClassWasCalled
    }

    fun registerCall(klass: Class<*>) {
        wasCalled[klass] = true
    }

    companion object {
        private val initialContext: InitialContext by lazy {
            InitialContext()
        }
    }
}
