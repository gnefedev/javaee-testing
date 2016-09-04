package com.gnefedev.javaee.test.cdi;

import com.gnefedev.javaee.testing.junit.JavaeeTestRunner;
import com.gnefedev.sample.StatelessExample;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * Created by gerakln on 14.08.16.
 */
@RequestScoped
@Stateful
@RunWith(JavaeeTestRunner.class)
public class TestInject {
    @Inject
    private StatelessExample stateless;

    @Test
    public void online() {
        assertEquals("Hello, World!!!", stateless.getGreetings());
    }
}
