package com.gnefedev.javaee.config;

import com.gnefedev.javaee.web.TestController;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by gerakln on 14.08.16.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = TestController.class)
public class WebConfig {
    @Bean
    public Marshaller marshaller() {
        return jaxb2Marshaller();
    }

    @Bean
    public Unmarshaller unmarshaller() {
        return jaxb2Marshaller();
    }

    @NotNull
    @Bean
    protected Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.gnefedev.javaee.model");
        return marshaller;
    }
}
