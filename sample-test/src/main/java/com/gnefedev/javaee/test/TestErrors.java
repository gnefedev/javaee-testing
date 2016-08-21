package com.gnefedev.javaee.test;

import com.gnefedev.javaee.junit.JavaeeTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.Stateless;

import static org.junit.Assert.assertTrue;

/**
 * Created by gerakln on 21.08.16.
 */
@Stateless
@RunWith(JavaeeTestRunner.class)
public class TestErrors {
    @Test
    public void assertFail() {
        assertTrue("assertFail message", false);
    }
}
