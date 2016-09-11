package com.gnefedev.javaee.test.cdi;

import com.gnefedev.javaee.testing.junit.JavaeeTestRunner;
import com.gnefedev.test.interceptor.InterceptedEjb;
import com.gnefedev.test.interceptor.HitInterceptor;
import com.gnefedev.test.interceptor.AnotherInterceptedEjb;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import static org.junit.Assert.*;

/**
 * Created by gerakln on 04.09.16.
 */
@RequestScoped
@Stateful
@RunWith(JavaeeTestRunner.class)
public class Interceptors {
    @Inject
    private InterceptedEjb firstInstance;
    @Inject
    private AnotherInterceptedEjb secondInstance;

    @Before
    public void before() {
        firstInstance.clear();
        secondInstance.clear();
    }

    @Test
    public void testCounter() {
        firstInstance.callToCount();
        assertEquals(1, firstInstance.countReplacedByInterceptor());
        firstInstance.callToCount();
        assertEquals(2, firstInstance.countReplacedByInterceptor());
        assertEquals(4, firstInstance.sumWithCount(2));

        assertEquals(0, secondInstance.countReplacedByInterceptor());
        secondInstance.callToCount();
        assertEquals(1, secondInstance.countReplacedByInterceptor());
    }

    @Test
    public void allTypes() {
        assertFalse(HitInterceptor.wasCalled);
        assertEquals(0, firstInstance.countReplacedByInterceptor());
        firstInstance.callToCount();
        assertEquals(1, firstInstance.countReplacedByInterceptor());
        assertTrue(HitInterceptor.wasCalled);
    }
}
