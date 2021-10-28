package org.opennms.ng.services.opennmsserverconfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServerConfigStream {
   final static String OPENNMS_SERVER_CONFIG_FILE_NAME_STR = "opennms-server.xml";
   public InputStream getConfig() throws IOException {
        return new FileInputStream(OPENNMS_SERVER_CONFIG_FILE_NAME_STR);
    }
}
