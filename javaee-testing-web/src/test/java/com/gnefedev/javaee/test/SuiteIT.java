package com.gnefedev.javaee.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Created by gerakln on 28.08.16.
 */
@RunWith(Suite.class)
@SuiteClasses({
        TestInject.class,
        TestLifecycle.class,
        TestStateful.class
})
public class SuiteIT {
}
