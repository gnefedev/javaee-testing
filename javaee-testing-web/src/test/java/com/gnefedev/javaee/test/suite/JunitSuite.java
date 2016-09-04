package com.gnefedev.javaee.test.suite;

import com.gnefedev.javaee.test.junit.TestErrorsWhenRun;
import com.gnefedev.javaee.test.junit.TestLifecycle;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Created by gerakln on 04.09.16.
 */
@RunWith(Suite.class)
@SuiteClasses({
        TestLifecycle.class,
        TestErrorsWhenRun.class
})
public class JunitSuite {
}
