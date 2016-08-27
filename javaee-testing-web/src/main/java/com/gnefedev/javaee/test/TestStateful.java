package com.gnefedev.javaee.test;

import com.gnefedev.javaee.junit.JavaeeTestRunner;
import com.gnefedev.sample.StatefulExample;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.ejb.Stateful;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * Created by gerakln on 27.08.16.
 */
@Stateful
@RunWith(JavaeeTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestStateful {
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
