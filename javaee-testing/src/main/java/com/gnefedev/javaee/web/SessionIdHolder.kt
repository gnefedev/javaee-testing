package com.gnefedev.javaee.web

import org.springframework.http.HttpHeaders

/**
 * Created by gerakln on 21.08.16.
 */
object SessionIdHolder {
    var sessionId: String? = null
    fun addSessionHeader(headers: HttpHeaders) {
        if (sessionId != null) {
            headers.add("Cookie", "JSESSIONID=" + sessionId)
        }
    }
}