package com.gnefedev.javaee.test;

import com.gnefedev.javaee.junit.JavaeeTestRunner;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.ejb.Stateless;

import static org.junit.Assert.assertTrue;

/**
 * Created by gerakln on 21.08.16.
 */
@Stateless
@RunWith(JavaeeTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestErrors {
    private static boolean throwExceptions = true;
    @Test
    public void _ifThisCalledTestWillNotFail() {
        throwExceptions = false;
    }
    @AfterClass
    public static void tearDown() {
        throwExceptions = true;
    }

    @Test
    public void assertFail() {
        if (throwExceptions) {
            assertTrue("assertFail message", false);
        }
    }
    @Test
    public void npe() {
        if (throwExceptions) {
            String nullable = null;
            nullable.length();
        }
    }
}
