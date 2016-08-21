package com.gnefedev.javaee.test;

import com.gnefedev.javaee.junit.JavaeeTestRunner;
import org.junit.*;
import org.junit.runner.RunWith;

import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;

import static org.junit.Assert.*;

/**
 * Created by gerakln on 21.08.16.
 */
@Stateful
@StatefulTimeout(5)
@RunWith(JavaeeTestRunner.class)
public class TestLifecycle {
    private static int beforeClassWasCalled = 0;
    private static int callsCount;
    private boolean beforeWasCalled = false;
    private boolean afterWasCalled = false;
    private String lastExecutedMethod = null;

    @BeforeClass
    public static void beforeClass() {
        beforeClassWasCalled++;
        callsCount = 0;
    }

    @Before
    public void setUp() {
        beforeWasCalled = true;
    }

    @After
    public void tearDown() {
        afterWasCalled = true;
        assertNotNull(lastExecutedMethod);
    }

    @AfterClass
    public static void afterClass() {
        beforeClassWasCalled = 0;
        assertEquals(2, callsCount);
    }

    @Test
    public void firstTest() {
        callsCount++;
        assertEquals(1, beforeClassWasCalled);
        assertTrue(beforeWasCalled);
        if (afterWasCalled) {
            assertEquals("secondTest", lastExecutedMethod);
        }
        lastExecutedMethod = "firstTest";
    }

    @Test
    public void secondTest() {
        callsCount++;
        assertEquals(1, beforeClassWasCalled);
        assertTrue(beforeWasCalled);
        if (afterWasCalled) {
            assertEquals("firstTest", lastExecutedMethod);
        }
        lastExecutedMethod = "secondTest";
    }
}
