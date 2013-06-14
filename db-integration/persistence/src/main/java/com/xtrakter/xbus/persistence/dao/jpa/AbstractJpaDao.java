package com.xtrakter.xbus.persistence.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractJpaDao {

    /**
     * The EntityManager
     */
    protected EntityManager em;

    /**
     * Sets the EntityManager through Spring injection.
     *
     * @param entityManager the new EntityManager
     */
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }
}