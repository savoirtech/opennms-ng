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

package org.opennms.ng.services.opennmsserverconfig;


import java.io.IOException;
import java.io.InputStream;


/**
 * This is the singleton class used to load the configuration for the OpenNMS
 * OutageManager from the outage-configuration xml file.
 *
 * <strong>Note: </strong>Users of this class should make sure the
 * <em>init()</em> is called before calling any other method to ensure the
 * config is loaded before accessing other convenience methods.
 *
 * @author <a href="mailto:jamesz@opennms.com">James Zuo </a>
 */
public class OpennmsServerConfigFactory extends OpennmsServerConfigManager {
    /**
     * The singleton instance of this factory
     */
    private static OpennmsServerConfigFactory m_singleton = null;
    final static String OPENNMS_SERVER_CONFIG_FILE_NAME_STR = "opennms-server.xml";

    /**
     * This member is set to true if the configuration file has been loaded.
     */
    private static boolean m_loaded = false;

    /**
     * <p>Constructor for OpennmsServerConfigFactory.</p>
     *
     * @param is a {@link java.io.InputStream} object.
     */
    public OpennmsServerConfigFactory(final InputStream is) {
        super(is);
    }

    /**
     * Load the config from the default config file and create the singleton
     * instance of this factory.
     *

     */
    public synchronized void init() throws IOException {
    }

    /**
     * Reload the config from the default config file
     *
     */
    public  synchronized void reload() throws IOException {
    }

    /**
     * Return the singleton instance of this factory.
     *
     * @return The current factory instance.
     * @throws IllegalStateException
     *             Thrown if the factory has not yet been initialized.
     */
    public static synchronized OpennmsServerConfigFactory getInstance() {
        return null;
    }
    
    /**
     * <p>setInstance</p>
     *
     * @param instance a {@link org.opennms.ng.services.opennmsserverconfig.OpennmsServerConfigFactory} object.
     */
    public static synchronized void setInstance(OpennmsServerConfigFactory instance) {
    }


}
