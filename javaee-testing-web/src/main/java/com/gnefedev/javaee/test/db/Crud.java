package com.gnefedev.javaee.test.db;

import com.gnefedev.javaee.testing.junit.JavaeeTestRunner;
import com.gnefedev.test.db.model.Animal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
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
@TransactionManagement(BEAN)
public class Crud {
    @PersistenceContext(unitName = "zoo")
    private EntityManager entityManager;
    @Resource
    private UserTransaction transaction;

    @Before
    public void createTransaction() throws Exception {
        transaction.begin();
    }

    @Test
    public void _01create() {
        Animal cow = new Animal();
        cow.setType("cow");
        cow.setWeight(200);
        entityManager.persist(cow);
    }

    @Test
    public void _02update() {
        Animal cow = findCow();
        assertEquals(200, cow.getWeight());
        cow.setWeight(210);
        entityManager.persist(cow);
    }

    @Test
    public void _03delete() {
        Animal cow = findCow();
        entityManager.remove(cow);
    }

    @Test
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

    @After
    public void commitTransaction() throws Exception {
        transaction.commit();
    }
}
