package org.opennms.ng.services.opennmsserverconfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.opennms.core.utils.ConfigFileConstants;

public class ServerConfigStream {

   public InputStream getConfig() throws IOException {
        return new FileInputStream(ConfigFileConstants.getFile(ConfigFileConstants.OPENNMS_SERVER_CONFIG_FILE_NAME));
    }
}
