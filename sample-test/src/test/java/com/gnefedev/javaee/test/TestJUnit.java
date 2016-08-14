package com.gnefedev.javaee.test;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import static org.junit.Assert.assertEquals;

/**
 * Created by gerakln on 14.08.16.
 */
public class TestJUnit {
    @Test
    public void test() {
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(SampleTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getTrace());
        }
        assertEquals(1, result.getFailureCount());
    }
}
