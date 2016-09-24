package com.gnefedev.javaee.test.db;

import com.gnefedev.jee.testing.junit.JavaeeTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.sql.DataSource;

/**
 * Created by gerakln
 * on 17.09.16.
 */
@RequestScoped
@Stateful
@RunWith(JavaeeTestRunner.class)
public class ResourceInject {
    @Resource(mappedName = "java:/jdbc/zoo")
    private DataSource dataSource;
    @Test
    public void checkDatasource() {
        Assert.assertNotNull(dataSource);
    }
}
