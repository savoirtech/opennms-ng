package org.opennms.ng.services.collectdconfig;

import java.util.Collection;

import org.opennms.netmgt.config.collectd.CollectdConfiguration;

public interface CollectdConfig {
    CollectdConfiguration getConfig();

    Collection<CollectdPackage> getPackages();

    int getThreads();

    void rebuildPackageIpListMap();

    CollectdPackage getPackage(String name);

    boolean domainExists(String name);

    boolean isServiceCollectionEnabled(String ipAddr, String svcName);
}
