package org.opennms.ng.persistence.dao.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Query;

import org.opennms.ng.persistence.dao.GenericDao;
import org.opennms.ng.persistence.notpurty.DataAccessException;

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

    //A simple implementation pretending to be HibernateTemplate...

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

    //TODO: This method duplicates below impl, delete this

    /**
     * Deletes a persistent object.
     *
     * @param persistentObject the persistent object
     */
    public void delete(T persistentObject) {
        em.remove(persistentObject);
        em.flush();
    }

    @Override
    public List find(String queryString) throws DataAccessException {
        return find(queryString, (Object[]) null);
    }

    @Override
    public List find(String queryString, Object value) throws DataAccessException {
        return find(queryString, new Object[]{value, null});
    }

    /**
     * <p>findUnique</p>
     *
     * @param query a {@link java.lang.String} object.
     * @return a T object.
     */
    protected T findUnique(final String query) {
        return findUnique(type, query);
    }

    /**
     * <p>findUnique</p>
     *
     * @param queryString a {@link java.lang.String} object.
     * @param args        a {@link java.lang.Object} object.
     * @return a T object.
     */
    protected T findUnique(final String queryString, final Object... args) {
        return findUnique(type, queryString, args);
    }

    /**
     * <p>findUnique</p>
     *
     * @param type        a {@link java.lang.Class} object.
     * @param queryString a {@link java.lang.String} object.
     * @param args        a {@link java.lang.Object} object.
     * @param <S>         a S object.
     * @return a S object.
     */

    protected <S> S findUnique(final Class<? extends S> type, final String queryString, final Object... args) {
        final Query query = em.createQuery(queryString);
        if (args != null) {
            for (int i = 0;i < args.length;i++) {
                query.setParameter(i, args[i]);
            }
        }
        final Object result = query.getSingleResult();
        return result == null ? null : type.cast(result);
    }

    @Override
    public List find(final String queryString, final Object... values) throws DataAccessException {
        Query queryObject = em.createQuery(queryString);
        if (values != null) {
            for (int i = 0;i < values.length;i++) {
                queryObject.setParameter(i + 1, values[i]);
            }
        }
        return queryObject.getResultList();
    }

    /**
     * <p>queryInt</p>
     *
     * @param query a {@link java.lang.String} object.
     * @return a int.
     */
    protected int queryInt(final String query) {

        Query q = em.createQuery(query);
        return Integer.valueOf(q.getSingleResult().toString());
    }

    /**
     * <p>queryInt</p>
     *
     * @param queryString a {@link java.lang.String} object.
     * @param args        a {@link java.lang.Object} object.
     * @return a int.
     */
    protected int queryInt(final String queryString, final Object... args) {

        final Query query = em.createQuery(queryString);
        if (args != null) {
            for (int i = 0;i < args.length;i++) {
                query.setParameter(i, args[i]);
            }
        }
        return Integer.valueOf(query.getSingleResult().toString());
    }

    /**
     * <p>findObjects</p>
     *
     * @param clazz  a {@link java.lang.Class} object.
     * @param query  a {@link java.lang.String} object.
     * @param values a {@link java.lang.Object} object.
     * @param <S>    a S object.
     * @return a {@link java.util.List} object.
     */
    @SuppressWarnings("unchecked")
    public <S> List<S> findObjects(final Class<S> clazz, final String query, final Object... values) {
        final List<S> notifs = find(query, values);
        return notifs;
    }

    public T get(final PK id) throws DataAccessException {
        return type.cast(em.find(type, id));
    }

    /**
     * {@inheritDoc}
     */
    public void initialize(final Object obj) {
        em.refresh(obj);
    }
}

