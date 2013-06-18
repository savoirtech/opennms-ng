package org.opennms.ng.services.eventconfig;

import java.util.concurrent.locks.Lock;

public interface EventdConfigManagement {
    Lock getReadLock();

    Lock getWriteLock();

    String getTCPIpAddress();

    int getTCPPort();

    String getUDPIpAddress();

    int getUDPPort();

    int getReceivers();

    int getQueueLength();

    String getSocketSoTimeoutRequired();

    int getSocketSoTimeoutPeriod();

    boolean hasSocketSoTimeoutPeriod();

    String getGetNextEventID();
}
