package org.opennms.ng.services.eventd2;

import java.io.IOException;

import org.opennms.core.utils.ConfigFileConstants;

public class EventdConfigStream {

   public String getConfig() throws IOException {
        return ConfigFileConstants.getFile(ConfigFileConstants.EVENTD_CONFIG_FILE_NAME).getAbsolutePath();
    }
}
