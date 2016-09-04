package com.gnefedev.javaee.test.junit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Created by gerakln on 25.08.16.
 */
public class TestLifecycleSuperClass {
    protected static boolean beforeClassInSuperClass = false;
    protected boolean beforeInSuperClass = false;
    @BeforeClass
    public static void beforeClassInSuperClass() {
        beforeClassInSuperClass = true;
    }
    @Before
    public void beforeInSuperClass() {
        beforeInSuperClass = true;
    }
    @AfterClass
    public static void afterClassInSuperClass() {
        beforeClassInSuperClass = false;
    }
}
