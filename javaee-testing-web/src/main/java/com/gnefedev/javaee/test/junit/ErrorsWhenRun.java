package com.gnefedev.javaee.test.junit;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import static org.junit.Assert.assertEquals;

/**
 * Created by gerakln on 04.09.16.
 */
public class ErrorsWhenRun {
    @Test
    public void testErrors() {
        JUnitCore jUnit = new JUnitCore();
        Result result = jUnit.run(TestWithErrors.class);
        assertEquals(2, result.getFailureCount());
        Failure assertFail = null;
        Failure npe = null;
        for (Failure failure : result.getFailures()) {
            String methodName = failure.getDescription().getMethodName();
            if (methodName.equals("assertFail")) {
                assertFail = failure;
            } else if (methodName.equals("npe")) {
                npe = failure;
            }
        }

        assertEquals("assertFail message", assertFail.getMessage());
        assertEquals(AssertionError.class, assertFail.getException().getClass());

        assertEquals(NullPointerException.class, npe.getException().getClass());
    }
}
