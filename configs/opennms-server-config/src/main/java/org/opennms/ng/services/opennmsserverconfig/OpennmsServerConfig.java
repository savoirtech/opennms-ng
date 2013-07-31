package org.opennms.ng.services.opennmsserverconfig;

public interface OpennmsServerConfig {
    String getServerName();

    String getDefaultCriticalPathIp();

    String getDefaultCriticalPathService();

    int getDefaultCriticalPathTimeout();

    int getDefaultCriticalPathRetries();

    boolean verifyServer();
}
