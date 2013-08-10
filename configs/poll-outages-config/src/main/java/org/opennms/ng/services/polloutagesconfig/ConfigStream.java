package org.opennms.ng.services.polloutagesconfig;

import java.io.IOException;

import org.opennms.core.utils.ConfigFileConstants;

public class ConfigStream {

   public String getConfig() throws IOException {
        return ConfigFileConstants.getFile(ConfigFileConstants.POLL_OUTAGES_CONFIG_FILE_NAME).getAbsolutePath();
    }
}
