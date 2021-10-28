
/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.ng.services.collectdconfig;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collection;
import org.opennms.netmgt.config.collectd.Collector;
import org.opennms.ng.services.opennmsserverconfig.OpennmsServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>CollectorConfigDaoImpl class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class CollectorConfigDaoImpl implements CollectorConfigDao {
    
    private static final Logger LOG = LoggerFactory.getLogger(CollectorConfigDaoImpl.class);

    private OpennmsServerConfig opennmsServerConfig;

    private CollectdConfigFactory collectdConfigFactory;

    /**
     * <p>Constructor for CollectorConfigDaoImpl.</p>
     */
    public CollectorConfigDaoImpl() {



    }

    public void loadConfigFactory() {
        // Load collectd configuration file
        try {
            // XXX was reload(); this doesn't work well from unit tests, however
            collectdConfigFactory =new CollectdConfigFactory(opennmsServerConfig);
            collectdConfigFactory.init();
        } catch (MarshalException ex) {
            LOG.error("loadConfigFactory: Failed to load collectd configuration", ex);
            throw new UndeclaredThrowableException(ex);
        } catch (ValidationException ex) {
            LOG.error("loadConfigFactory: Failed to load collectd configuration", ex);
            throw new UndeclaredThrowableException(ex);
        } catch (IOException ex) {
            LOG.error("loadConfigFactory: Failed to load collectd configuration", ex);
            throw new UndeclaredThrowableException(ex);
        }
    }

    private CollectdConfig getConfig() {
        return collectdConfigFactory.getInstance().getCollectdConfig();
    }

    public CollectdConfig agetConfig() {
            return collectdConfigFactory.getInstance().getCollectdConfig();
        }


    /**
     * <p>getSchedulerThreads</p>
     *
     * @return a int.
     */
    @Override
    public int getSchedulerThreads() {
        return getConfig().getThreads();
    }


    /**
     * <p>getCollectors</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    @Override
    public Collection<Collector> getCollectors() {
        return getConfig().getConfig().getCollectorCollection();
    }

    /**
     * <p>rebuildPackageIpListMap</p>
     */
    @Override
    public void rebuildPackageIpListMap() {
        getConfig().rebuildPackageIpListMap();
    }
    
    /**
     * <p>getPackages</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    @Override
    public Collection<CollectdPackage> getPackages() {
        return getConfig().getPackages();
    }
    
    /** {@inheritDoc} */
    @Override
    public CollectdPackage getPackage(String name) {
        return getConfig().getPackage(name);
    }

    public OpennmsServerConfig getOpennmsServerConfig() {
        return opennmsServerConfig;
    }

    public void setOpennmsServerConfig(OpennmsServerConfig opennmsServerConfig) {
        this.opennmsServerConfig = opennmsServerConfig;
    }
}
