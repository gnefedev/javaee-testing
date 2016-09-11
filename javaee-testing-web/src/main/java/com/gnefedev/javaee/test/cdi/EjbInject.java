package com.gnefedev.javaee.test.cdi;

import com.gnefedev.javaee.testing.junit.JavaeeTestRunner;
import com.gnefedev.test.simple.Animal;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;

import static org.junit.Assert.assertEquals;

/**
 * Created by gerakln on 11.09.16.
 */
@RequestScoped
@Stateful
@RunWith(JavaeeTestRunner.class)
public class EjbInject {
    @EJB(beanName = "dog")
    private Animal dog;
    @EJB(beanName = "cat")
    private Animal cat;

    @Test
    public void animals() {
        assertEquals("bark", dog.sound());
        assertEquals("meaou", cat.sound());
    }
}
