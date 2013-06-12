package org.opennms.ng.persistence.dao;

public interface VersionDao extends GenericDao<String,String> {
    public String getVersionNumber();
}
