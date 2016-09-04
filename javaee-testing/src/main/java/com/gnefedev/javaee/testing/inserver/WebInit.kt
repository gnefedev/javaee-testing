package com.gnefedev.javaee.testing.inserver

import org.springframework.web.WebApplicationInitializer
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet
import javax.servlet.ServletContext
import javax.servlet.ServletException

/**
 * Created by gerakln on 14.08.16.
 */
class WebInit : WebApplicationInitializer {
    @Throws(ServletException::class)
    override fun onStartup(servletContext: ServletContext) {
        val context = AnnotationConfigWebApplicationContext()
        context.register(WebConfig::class.java)
        servletContext.addListener(ContextLoaderListener(context))
        val dispatcher = servletContext.addServlet("onlineTestServlet", DispatcherServlet(context))
        dispatcher.setLoadOnStartup(1)
        dispatcher.addMapping("/*")
    }
}
