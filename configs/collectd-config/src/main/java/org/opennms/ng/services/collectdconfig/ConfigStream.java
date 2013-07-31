package org.opennms.ng.services.collectdconfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.opennms.core.utils.ConfigFileConstants;

public class ConfigStream {

   public InputStream getConfig() throws IOException {
        return new FileInputStream(ConfigFileConstants.getFile(ConfigFileConstants.CAPSD_CONFIG_FILE_NAME));
    }
}
