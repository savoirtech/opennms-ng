package com.xtrakter.xbus.persistence.dao;

public interface VersionDao extends GenericDao<String,String> {
    public String getVersionNumber();
}
