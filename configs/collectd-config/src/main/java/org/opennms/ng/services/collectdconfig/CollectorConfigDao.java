package org.opennms.ng.services.collectdconfig;

import java.util.Collection;

import org.opennms.netmgt.config.collectd.Collector;

public interface CollectorConfigDao {

    /**
     * <p>getSchedulerThreads</p>
     *
     * @return a int.
     */
    int getSchedulerThreads();

    /**
     * <p>getCollectors</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    Collection<Collector> getCollectors();

    /**
     * <p>rebuildPackageIpListMap</p>
     */
    void rebuildPackageIpListMap();

    /**
     * <p>getPackages</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    Collection<CollectdPackage> getPackages();

    /**
     * <p>getPackage</p>
     *
     *
     * @param name a {@link String} object.
     * @return a {@link org.opennms.netmgt.config.CollectdPackage} object.
     */
    CollectdPackage getPackage(String name);

}
