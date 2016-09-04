package com.gnefedev.javaee.test.suite;

import com.gnefedev.javaee.test.cdi.TestInject;
import com.gnefedev.javaee.test.cdi.TestInterceptor;
import com.gnefedev.javaee.test.cdi.TestStateful;
import com.gnefedev.javaee.test.cdi.TestStatefulCopy;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Created by gerakln on 04.09.16.
 */
@RunWith(Suite.class)
@SuiteClasses({
        TestInject.class,
        TestStateful.class,
        TestStatefulCopy.class,
        TestInterceptor.class
})
public class CdiSuite {
}
