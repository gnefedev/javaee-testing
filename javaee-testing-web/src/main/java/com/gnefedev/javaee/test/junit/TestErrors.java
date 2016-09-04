package com.gnefedev.javaee.test.junit;

import com.gnefedev.javaee.testing.junit.JavaeeTestRunner;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;

import static org.junit.Assert.assertTrue;

/**
 * Created by gerakln on 21.08.16.
 */
@RequestScoped
@Stateful
@RunWith(JavaeeTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestErrors {

    @Test
    public void assertFail() {
        assertTrue("assertFail message", false);
    }

    @Test
    public void npe() {
        String nullable = null;
        nullable.length();
    }
}
