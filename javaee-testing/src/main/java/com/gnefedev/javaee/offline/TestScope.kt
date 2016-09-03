package com.gnefedev.javaee.offline

import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.config.Scope


/**
 * Created by gerakln on 03.09.16.
 */
internal object TestScope : Scope {
    private val objectMap: MutableMap<String, Any> = mutableMapOf()

    override fun resolveContextualObject(key: String?): Any? {
        return null
    }

    override fun remove(name: String): Any? {
        val previous = objectMap[name]
        objectMap.remove(name)
        return previous
    }

    override fun registerDestructionCallback(name: String?, callback: Runnable?) {
    }

    override fun getConversationId(): String {
        return "test"
    }

    override fun get(name: String, factory: ObjectFactory<*>): Any {
        return objectMap.getOrPut(name, { factory.`object` })
    }

    fun clear() = objectMap.clear()
}