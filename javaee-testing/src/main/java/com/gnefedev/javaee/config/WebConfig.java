package com.gnefedev.javaee.config;

import com.gnefedev.javaee.web.TestController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by gerakln on 14.08.16.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = TestController.class)
public class WebConfig {
}
