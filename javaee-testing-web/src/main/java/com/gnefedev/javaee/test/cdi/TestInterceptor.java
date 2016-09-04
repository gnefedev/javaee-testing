package com.gnefedev.javaee.test.cdi;

import com.gnefedev.javaee.testing.junit.JavaeeTestRunner;
import com.gnefedev.test.interceptor.InterceptedEjb;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * Created by gerakln on 04.09.16.
 */
@RequestScoped
@Stateful
@RunWith(JavaeeTestRunner.class)
public class TestInterceptor {
    @Inject
    private InterceptedEjb interceptedEjb;

    @Test
    public void testCounter() {
        interceptedEjb.callToCount();
        assertEquals(1, interceptedEjb.countReplacedByInterceptor());
        interceptedEjb.callToCount();
        assertEquals(2, interceptedEjb.countReplacedByInterceptor());
    }
}
