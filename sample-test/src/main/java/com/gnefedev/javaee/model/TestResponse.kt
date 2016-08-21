package com.gnefedev.javaee.model

/**
 * Created by gerakln on 21.08.16.
 */
data class TestResponse(val error: Boolean, val message: String) {
    constructor() : this(false, "")
}