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

package org.opennms.ng.services.eventconfig;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.io.IOUtils;
import org.opennms.netmgt.config.eventd.EventdConfiguration;

/**
 * <p>EventdConfigManager class.</p>
 *
 * @author david
 */
public class EventdConfigManager implements EventdConfigManagement {
    private final ReadWriteLock m_globalLock = new ReentrantReadWriteLock();
    private final Lock m_readLock = m_globalLock.readLock();
    private final Lock m_writeLock = m_globalLock.writeLock();

    /**
     * The config class loaded from the config file
     */
    protected EventdConfiguration m_config;

    /**
     * <p>Constructor for EventdConfigManager.</p>
     *
     * @param stream a {@link java.io.InputStream} object.
     * @throws org.exolab.castor.xml.MarshalException if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     * @throws java.io.IOException if any.
     */
    public EventdConfigManager(final InputStream stream) throws MarshalException, ValidationException, IOException {
        m_config = CastorUtils.unmarshal(EventdConfiguration.class, stream);

    }
    
    /**
     * <p>Constructor for EventdConfigManager.</p>
     *
     * @param configFile a {@link String} object.
     * @throws java.io.FileNotFoundException if any.
     * @throws org.exolab.castor.xml.MarshalException if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     */
    public EventdConfigManager(final String configFile) throws FileNotFoundException, MarshalException, ValidationException {
        InputStream stream = null;
        try {
            stream = new FileInputStream(configFile);
            m_config = CastorUtils.unmarshal(EventdConfiguration.class, stream);
        } finally {
            if (stream != null) {
                IOUtils.closeQuietly(stream);
            }
        }
    }

    @Override
    public Lock getReadLock() {
        return m_readLock;
    }
    
    @Override
    public Lock getWriteLock() {
        return m_writeLock;
    }

    /**
     * Return the IP address on which eventd listens for TCP connections.
     *
     * @return the IP address on which eventd listens for TCP connections
     */
    @Override
    public String getTCPIpAddress() {
        getReadLock().lock();
        try {
            return m_config.getTCPAddress();
        } finally {
            getReadLock().unlock();
        }
    }
    
    /**
     * Return the port on which eventd listens for TCP connections.
     *
     * @return the port on which eventd listens for TCP connections
     */
    @Override
    public int getTCPPort() {
        getReadLock().lock();
        try {
            return m_config.getTCPPort();
        } finally {
            getReadLock().unlock();
        }
    }

    /**
     * Return the IP address on which eventd listens for UDP packets.
     *
     * @return the IP address on which eventd listens for UDP packets
     */
    @Override
    public String getUDPIpAddress() {
        getReadLock().lock();
        try {
            return m_config.getUDPAddress();
        } finally {
            getReadLock().unlock();
        }
    }

    /**
     * Return the port on which eventd listens for UDP data.
     *
     * @return the port on which eventd listens for UDP data
     */
    @Override
    public int getUDPPort() {
        getReadLock().lock();
        try {
            return m_config.getUDPPort();
        } finally {
            getReadLock().unlock();
        }
    }

    /**
     * Return the number of event receivers to be started.
     *
     * @return the number of event receivers to be started
     */
    @Override
    public int getReceivers() {
        getReadLock().lock();
        try {
            return m_config.getReceivers();
        } finally {
            getReadLock().unlock();
        }
    }

    /**
     * Return the length of the incoming event queue.
     *
     * @return the maximum number of events that can be stored in the incoming event queue
     */
    @Override
    public int getQueueLength() {
        getReadLock().lock();
        try {
            return m_config.hasQueueLength() ? m_config.getQueueLength() : Integer.MAX_VALUE;
        } finally {
            getReadLock().unlock();
        }
    }

    /**
     * Return string indicating if timeout is to be set on the socket.
     *
     * @return string indicating if timeout is to be set on the socket
     */
    @Override
    public String getSocketSoTimeoutRequired() {
        getReadLock().lock();
        try {
            return m_config.getSocketSoTimeoutRequired();
        } finally {
            getReadLock().unlock();
        }
    }

    /**
     * Return timeout to be set on the socket.
     *
     * @return timeout is to be set on the socket
     */
    @Override
    public int getSocketSoTimeoutPeriod() {
        getReadLock().lock();
        try {
            return m_config.getSocketSoTimeoutPeriod();
        } finally {
            getReadLock().unlock();
        }
    }

    /**
     * Return flag indicating if timeout to be set on the socket is specified.
     *
     * @return flag indicating if timeout to be set on the socket is specified <
     */
    @Override
    public boolean hasSocketSoTimeoutPeriod() {
        getReadLock().lock();
        try {
            return m_config.hasSocketSoTimeoutPeriod();
        } finally {
            getReadLock().unlock();
        }
    }

    /**
     * Return the SQL statement to get the next event ID.
     *
     * @return the SQL statement to get the next event ID
     */
    @Override
    public String getGetNextEventID() {
        getReadLock().lock();
        try {
            return m_config.getGetNextEventID();
        } finally {
            getReadLock().unlock();
        }
    }
}
