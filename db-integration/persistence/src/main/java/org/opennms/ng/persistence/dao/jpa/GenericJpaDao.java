package com.xtrakter.xbus.persistence.dao.jpa;

import com.xtrakter.xbus.persistence.dao.GenericDao;

import java.io.Serializable;

public abstract class GenericJpaDao<T, PK extends Serializable> extends AbstractJpaDao implements GenericDao<T, PK> {

    /**
     * The generic class type.
     */
    private Class<T> type;

    /**
     * Instantiates a new generic JPA DAO implementation.
     *
     * @param type the type
     */
    public GenericJpaDao(Class<T> type) {
        this.type = type;
    }

    /**
     * Creates a new instance of a persistent object.
     *
     * @param newInstance the new instance
     */
    public void create(T newInstance) {
        em.persist(newInstance);
        em.flush();
    }

    /**
     * Reads a persistent object.
     *
     * @param id the id
     * @return the t
     */
    public T read(PK id) {
        return em.find(type, id);
    }

    /**
     * Updates a persistent object.
     *
     * @param persistentObject the persistent object
     */
    public void update(T persistentObject) {
        em.merge(persistentObject);
        em.flush();
    }

    /**
     * Deletes a persistent object.
     *
     * @param persistentObject the persistent object
     */
    public void delete(T persistentObject) {
        em.remove(persistentObject);
        em.flush();
    }
}