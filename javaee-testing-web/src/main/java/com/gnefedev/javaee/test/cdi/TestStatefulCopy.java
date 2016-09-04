package com.gnefedev.javaee.test.cdi;

import com.gnefedev.javaee.testing.junit.JavaeeTestRunner;
import com.gnefedev.test.simple.StatefulExample;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * Created by gerakln on 27.08.16.
 */
@RequestScoped
@Stateful
@RunWith(JavaeeTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestStatefulCopy {
    @Inject
    private StatefulExample stateful;

    @Test
    public void firstCall() {
        assertEquals(0, stateful.getCallCount());
        stateful.increment();
        assertEquals(1, stateful.getCallCount());
    }

    @Test
    public void secondCall() {
        assertEquals(1, stateful.getCallCount());
    }
}
