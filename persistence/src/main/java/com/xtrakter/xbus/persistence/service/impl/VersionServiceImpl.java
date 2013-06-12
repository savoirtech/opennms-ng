package org.opennms.ng.persistence.service.impl;

import org.opennms.ng.persistence.dao.VersionDao;
import org.opennms.ng.persistence.service.VersionService;

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
