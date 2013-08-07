package org.opennms.ng.persistence.notpurty;

import java.util.List;

public interface JpaCommands {

    public List find(java.lang.String queryString) throws DataAccessException;

    public List find(java.lang.String queryString, java.lang.Object value) throws DataAccessException;

    public List find(java.lang.String queryString, java.lang.Object... values) throws DataAccessException;
}
