package com.gnefedev.javaee.test.suite;

import com.gnefedev.javaee.test.db.Crud;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Created by gerakln on 04.09.16.
 */
@RunWith(Suite.class)
@SuiteClasses({
        Crud.class,
})
public class DbSuite {
}
