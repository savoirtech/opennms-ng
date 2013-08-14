package org.opennms.ng.services.collectd;

import java.util.Collection;

import org.opennms.netmgt.config.collectd.Collector;
import org.opennms.ng.services.collectdconfig.CollectdPackage;

public interface CollectorConfigDao {
    int getSchedulerThreads();

    Collection<Collector> getCollectors();

    void rebuildPackageIpListMap();

    Collection<CollectdPackage> getPackages();

    CollectdPackage getPackage(String name);
}
