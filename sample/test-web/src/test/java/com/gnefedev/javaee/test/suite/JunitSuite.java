package com.gnefedev.javaee.test.suite;

import com.gnefedev.javaee.test.junit.ErrorsWhenRun;
import com.gnefedev.javaee.test.junit.LifecycleOfTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Created by gerakln on 04.09.16.
 */
@RunWith(Suite.class)
@SuiteClasses({
        LifecycleOfTest.class,
        ErrorsWhenRun.class
})
public class JunitSuite {
}
