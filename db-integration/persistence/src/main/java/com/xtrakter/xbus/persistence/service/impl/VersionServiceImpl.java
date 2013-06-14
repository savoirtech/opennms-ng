package com.xtrakter.xbus.persistence.service.impl;

import com.xtrakter.xbus.persistence.dao.VersionDao;
import com.xtrakter.xbus.persistence.service.VersionService;

public class VersionServiceImpl implements VersionService{

    private VersionDao versionDao = null;

    @Override
    public String getVersionNumber() {
        return versionDao.getVersionNumber();
    }

    public void setVersionDao(VersionDao versionDao) {
        this.versionDao = versionDao;
    }
}
