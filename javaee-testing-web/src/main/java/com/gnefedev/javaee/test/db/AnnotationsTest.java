package com.gnefedev.javaee.test.db;

import com.gnefedev.javaee.testing.junit.JavaeeTestRunner;
import com.gnefedev.test.db.model.Animal;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import static javax.ejb.TransactionManagementType.BEAN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by gerakln on 11.09.16.
 */
@RequestScoped
@Stateful
@RunWith(JavaeeTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AnnotationsTest {
    @PersistenceContext(unitName = "zoo")
    private EntityManager entityManager;

    @Test
    @Transactional
    public void _01create() {
        Animal cow = new Animal();
        cow.setType("cow");
        cow.setWeight(200);
        entityManager.persist(cow);
    }

    @Test
    @Transactional
    public void _02update() {
        Animal cow = findCow();
        assertEquals(200, cow.getWeight());
        cow.setWeight(210);
        entityManager.persist(cow);
    }

    @Test
    @Transactional
    public void _03delete() {
        Animal cow = findCow();
        entityManager.remove(cow);
    }

    @Test
    @Transactional
    public void _04weNotHaveCow() {
        try {
            findCow();
            fail();
        } catch (NoResultException ignored) {
        }
    }

    private Animal findCow() {
        return entityManager
                .createQuery("select c from Animal c where c.type = :type", Animal.class)
                .setParameter("type", "cow")
                .getSingleResult();
    }
}
