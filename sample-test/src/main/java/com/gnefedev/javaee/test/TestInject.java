package com.gnefedev.javaee.test;

import com.gnefedev.javaee.junit.JavaeeTestRunner;
import com.gnefedev.sample.HelloWorld;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.Stateless;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * Created by gerakln on 14.08.16.
 */
@Stateless
@RunWith(JavaeeTestRunner.class)
public class TestInject {
    @Inject
    private HelloWorld fromEjb;

    @Test
    public void online() {
        assertEquals("Hello, World!!!", fromEjb.getGreetings());
    }
}
