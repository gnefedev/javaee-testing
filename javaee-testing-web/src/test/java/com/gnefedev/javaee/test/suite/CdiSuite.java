package com.gnefedev.javaee.test.suite;

import com.gnefedev.javaee.test.cdi.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Created by gerakln on 04.09.16.
 */
@RunWith(Suite.class)
@SuiteClasses({
        SimpleInject.class,
        TestOfStateful.class,
        TestOfStatefulCopy.class,
        Interceptors.class,
        EjbInject.class
})
public class CdiSuite {
}
