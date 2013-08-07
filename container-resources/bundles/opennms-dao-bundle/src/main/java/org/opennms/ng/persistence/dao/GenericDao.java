package org.opennms.ng.persistence.dao;

import java.io.Serializable;

import org.opennms.ng.persistence.notpurty.JpaCommands;

public interface GenericDao<T, PK extends Serializable> extends JpaCommands {

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

    //Opennms API specific additions.
}