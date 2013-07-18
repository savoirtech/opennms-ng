package org.opennms.ng.services.trapd;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.opennms.core.utils.ConfigFileConstants;

public class TrapdConfigStream {

   public InputStream getConfig() throws IOException {
        return new FileInputStream(ConfigFileConstants.getFile(ConfigFileConstants.TRAPD_CONFIG_FILE_NAME));
    }
}
