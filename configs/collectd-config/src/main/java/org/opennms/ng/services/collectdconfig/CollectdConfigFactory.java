/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.ConfigFileConstants;
import org.opennms.netmgt.config.collectd.CollectdConfiguration;
import org.opennms.ng.services.opennmsserverconfig.OpennmsServerConfig;
import org.opennms.ng.util.xml.CastorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author <a href="mailto:jamesz@opennms.com">James Zuo</a>
 * @author <a href="mailto:mike@opennms.org">Mike Davidson</a>
 * @author <a href="mailto:sowmya@opennms.org">Sowmya Nataraj</a>
 */

/**
 * This is the singleton class used to load the configuration for the OpenNMS
 * Collection Daemon from the collectd-configuration xml file.
 * <p/>
 * A mapping of the configured URLs to the iplist they contain is built at
 * init() time so as to avoid numerous file reads.
 * <p/>
 * <strong>Note: </strong>Users of this class should make sure the
 * <em>init()</em> is called before calling any other method to ensure the
 * config is loaded before accessing other convenience methods.
 *
 * @author ranger
 * @version $Id: $
 */
public class CollectdConfigFactory {
    public final static String SELECT_METHOD_MIN = "min";
    private static final Logger LOG = LoggerFactory.getLogger(CollectdConfigFactory.class);
    /**
     * The singleton instance of this factory.  Null if the factory hasn't been
     * initialized.
     */
    private static CollectdConfigFactory m_singleton = null;

    /**
     * A boolean flag to indicate If a filter rule against the local NMS server
     * has to be used.
     */
    //private static boolean m_verifyServer;
    /**
     * Name of the local NMS server.
     */
    //private static String m_localServer;

    private OpennmsServerConfig opennmsServerConfig;
    private CollectdConfig m_collectdConfig;


    public CollectdConfigFactory(OpennmsServerConfig opennmsServerConfigFactory) {
        this.opennmsServerConfig=opennmsServerConfigFactory;
    }

    /**
     * Private constructor
     *
     * @param verifyServer
     * @param localServer
     * @throws java.io.IOException                       Thrown if the specified config file cannot be read
     * @throws org.exolab.castor.xml.MarshalException    Thrown if the file does not conform to the schema.
     * @throws org.exolab.castor.xml.ValidationException Thrown if the contents do not match the required schema.
     */
    private CollectdConfigFactory(String configFile, String localServer,
                                  boolean verifyServer,OpennmsServerConfig opennmsServerConfigFactory) throws IOException, MarshalException, ValidationException {
        InputStream stream = null;
        this.opennmsServerConfig = opennmsServerConfigFactory;
        try {
            stream = new FileInputStream(configFile);
            CollectdConfiguration config = CastorUtils.unmarshal(CollectdConfiguration.class, stream);
            m_collectdConfig = new CollectdConfigImpl(config, localServer, verifyServer);
        } finally {
            if (stream != null) {
                IOUtils.closeQuietly(stream);
            }
        }
    }



    /**
     * <p>Constructor for CollectdConfigFactory.</p>
     *
     * @param stream       a {@link java.io.InputStream} object.
     * @param localServer  a {@link String} object.
     * @param verifyServer a boolean.
     * @throws org.exolab.castor.xml.MarshalException    if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     */
    public CollectdConfigFactory(InputStream stream, String localServer, boolean verifyServer,OpennmsServerConfig opennmsServerConfig) throws MarshalException, ValidationException {
        CollectdConfiguration config = CastorUtils.unmarshal(CollectdConfiguration.class, stream);
        m_collectdConfig = new CollectdConfigImpl(config, localServer, verifyServer);
        this.opennmsServerConfig=opennmsServerConfig;
    }

    /**
     * Return the singleton instance of this factory.
     *
     * @return The current factory instance.
     * @throws IllegalStateException Thrown if the factory has not yet been initialized.
     */
    public static synchronized CollectdConfigFactory getInstance() {
        Assert.state(isInitialized(), "The factory has not been initialized");

        return m_singleton;
    }

    /**
     * <p>setInstance</p>
     *
     * @param instance a {@link org.opennms.ng.services.collectdconfig.CollectdConfigFactory} object.
     */
    public synchronized void setInstance(CollectdConfigFactory instance) {
        m_singleton = instance;
    }

    private static boolean isInitialized() {
        return m_singleton != null;
    }

    /**
     * Load the config from the default config file and create the singleton
     * instance of this factory.
     *
     * @throws java.io.IOException                       Thrown if the specified config file cannot be read
     * @throws org.exolab.castor.xml.MarshalException    Thrown if the file does not conform to the schema.
     * @throws org.exolab.castor.xml.ValidationException Thrown if the contents do not match the required schema.
     * @throws java.io.IOException                       if any.
     * @throws org.exolab.castor.xml.MarshalException    if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     */
    public synchronized void init() throws IOException, MarshalException, ValidationException {
        if (isInitialized()) {
            // init already called return; to reload, reload() will need to be called
            return;
        }

        File cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.COLLECTD_CONFIG_FILE_NAME);

        LOG.debug("init: config file path: {}", cfgFile.getPath());
        System.out.println("Config factory " + opennmsServerConfig);

        setInstance(new CollectdConfigFactory(cfgFile.getPath(), opennmsServerConfig.getServerName(), opennmsServerConfig.verifyServer(),opennmsServerConfig));
    }

    /**
     * Reload the config from the default config file
     *
     * @throws java.io.IOException                       Thrown if the specified config file cannot be read/loaded
     * @throws org.exolab.castor.xml.MarshalException    Thrown if the file does not conform to the schema.
     * @throws org.exolab.castor.xml.ValidationException Thrown if the contents do not match the required schema.
     * @throws java.io.IOException                       if any.
     * @throws org.exolab.castor.xml.MarshalException    if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     */
    public synchronized void reload() throws IOException, MarshalException, ValidationException {
        m_singleton = null;

        init();
    }

    /**
     * This method is used to rebuild the package agaist iplist mapping when
     * needed. When a node gained service event occurs, collectd has to
     * determine which package the ip/service combination is in, but if the
     * interface is a newly added one, the package iplist should be rebuilt so
     * that collectd could know which package this ip/service pair is in.
     */
    public synchronized void rebuildPackageIpListMap() {
        m_collectdConfig.rebuildPackageIpListMap();
    }

    /**
     * Saves the current in-memory configuration to disk and reloads
     *
     * @throws org.exolab.castor.xml.MarshalException    if any.
     * @throws java.io.IOException                       if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     */
    public synchronized void saveCurrent() throws MarshalException, IOException, ValidationException {
        File cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.COLLECTD_CONFIG_FILE_NAME);

        CollectdConfiguration config = m_collectdConfig.getConfig();

        CastorUtils.marshalViaString(config, cfgFile);

        reload();
    }

    /**
     * <p>getCollectdConfig</p>
     *
     * @return a {@link CollectdConfigImpl} object.
     */
    public CollectdConfig getCollectdConfig() {
        return m_collectdConfig;
    }

    /**
     * <p>getPackage</p>
     *
     * @param name a {@link String} object.
     * @return a {@link CollectdPackage} object.
     */
    public CollectdPackage getPackage(String name) {
        return m_collectdConfig.getPackage(name);
    }

    /**
     * Returns true if collection package exists
     *
     * @param name The package name to check
     * @return True if the package exists
     */
    public synchronized boolean packageExists(String name) {
        return m_collectdConfig.getPackage(name) != null;
    }

    /**
     * Returns true if collection domain exists
     *
     * @param name The domain name to check
     * @return True if the domain exists
     */
    public boolean domainExists(String name) {
        return m_collectdConfig.domainExists(name);
    }

    /**
     * Returns true if the specified interface is included by at least one
     * package which has the specified service and that service is enabled (set
     * to "on").
     *
     * @param ipAddr  IP address of the interface to lookup
     * @param svcName The service name to lookup
     * @return true if Collectd config contains a package which includes the
     * specified interface and has the specified service enabled.
     */
    public boolean isServiceCollectionEnabled(final String ipAddr, final String svcName) {
        return m_collectdConfig.isServiceCollectionEnabled(ipAddr, svcName);
    }
}
