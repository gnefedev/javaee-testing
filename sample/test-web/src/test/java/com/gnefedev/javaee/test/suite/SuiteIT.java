package com.gnefedev.javaee.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Created by gerakln on 28.08.16.
 */
@RunWith(Suite.class)
@SuiteClasses({
        CdiSuite.class,
        JunitSuite.class,
        DbSuite.class,
        MqSuite.class
})
public class SuiteIT {
}
