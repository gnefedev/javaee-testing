package com.gnefedev.javaee.test;

import com.gnefedev.javaee.junit.JavaeeTestRunner;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;

import static org.junit.Assert.*;

/**
 * Created by gerakln on 21.08.16.
 */
@Stateful
@StatefulTimeout(5)
@RunWith(JavaeeTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLifecycle extends TestLifecycleSuperClass {
    private static int beforeClassWasCalled = 0;
    private static int callsCount;
    private static int beforeWasCalled = 0;
    private boolean afterWasCalled = false;
    private String lastExecutedMethod = null;

    @BeforeClass
    public static void beforeClass() {
        beforeClassWasCalled++;
        beforeWasCalled = 0;
        callsCount = 0;
    }

    @Before
    public void setUp() {
        beforeWasCalled++;
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
        assertTrue(beforeClassInSuperClass);
        assertTrue(beforeInSuperClass);
        assertEquals(1, beforeClassWasCalled);
        assertEquals(1, beforeWasCalled);
        assertNull(lastExecutedMethod);
        lastExecutedMethod = "firstTest";
    }

    @Test
    public void secondTest() {
        callsCount++;
        assertEquals(1, beforeClassWasCalled);
        assertEquals(2, beforeWasCalled);
        assertTrue(afterWasCalled);
        assertEquals("firstTest", lastExecutedMethod);
        lastExecutedMethod = "secondTest";
    }
}
