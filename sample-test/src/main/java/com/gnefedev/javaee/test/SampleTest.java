package com.gnefedev.javaee.test;

import com.gnefedev.javaee.junit.JavaeeTestRunner;
import com.gnefedev.sample.FromCore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.Stateless;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * Created by gerakln on 14.08.16.
 */
@Stateless
@RunWith(JavaeeTestRunner.class)
public class SampleTest {
    @Inject
    private FromCore fromCore;

    @Test
    public void offline() {
        assertEquals("Hello, World!!!", new FromCore().getGreetings());
    }

    @Test
    public void online() {
        assertEquals("Hello, World!!!", fromCore.getGreetings());
    }
}
