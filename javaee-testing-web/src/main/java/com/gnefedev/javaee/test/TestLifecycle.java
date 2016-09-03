package com.gnefedev.javaee.test;

import com.gnefedev.javaee.junit.JavaeeTestRunner;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.ejb.Stateless;

import static org.junit.Assert.*;

/**
 * Created by gerakln on 21.08.16.
 */
@Stateless
@RunWith(JavaeeTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLifecycle extends TestLifecycleSuperClass {
    private static int beforeClassWasCalled = 0;
    private static int callsCount;
    private static int beforeWasCalled;
    private static boolean afterWasCalled;
    private static String lastExecutedMethod;
    private String shouldNotSaved = null;

    @BeforeClass
    public static void beforeClass() {
        callsCount = 0;
        beforeWasCalled = 0;
        afterWasCalled = false;
        lastExecutedMethod = null;

        beforeClassWasCalled++;
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
        assertNull(shouldNotSaved);
        shouldNotSaved = "firstTest";

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
        assertNull(shouldNotSaved);
        shouldNotSaved = "secondTest";

        assertEquals(1, beforeClassWasCalled);
        assertEquals(2, beforeWasCalled);
        assertTrue(afterWasCalled);
        assertEquals("firstTest", lastExecutedMethod);
        lastExecutedMethod = "secondTest";
    }
}
