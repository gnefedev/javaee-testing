package com.gnefedev.javaee.test.cdi;

import com.gnefedev.jee.testing.junit.JavaeeTestRunner;
import com.gnefedev.test.simple.animals.Animal;
import com.gnefedev.test.simple.animals.Biggest;
import com.gnefedev.test.simple.animals.King;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

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
    @EJB(beanName = "Bear")
    private Animal bear;
    @King
    @Inject
    private Animal king;
    @Biggest
    @Inject
    private Animal elephantOrBear;

    @Test
    public void statelessNames() {
        assertEquals("bark", dog.sound());
        assertEquals("meaou", cat.sound());
        assertEquals("gr", bear.sound());
    }

    @Test
    public void qualifiersWithAlternatives() {
        assertEquals("r-r-r", king.sound());
        assertEquals("ugh", elephantOrBear.sound());
    }
}
