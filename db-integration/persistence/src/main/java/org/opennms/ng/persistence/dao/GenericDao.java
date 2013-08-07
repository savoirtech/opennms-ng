package com.xtrakter.xbus.persistence.dao;

import java.io.Serializable;

public interface GenericDao<T, PK extends Serializable> {

    /**
     * Creates a new instance of a persistent object.
     *
     * @param newInstance the new instance
     */
    public void create(T newInstance);

    /**
     * Reads a persistent object.
     *
     * @param id the id
     * @return the t
     */
    public T read(PK id);

    /**
     * Updates a persistent object.
     *
     * @param persistentObject the persistent object
     */
    public void update(T persistentObject);

    /**
     * Deletes a persistent object.
     *
     * @param persistentObject the persistent object
     */
    public void delete(T persistentObject);
}