package org.opennms.ng.services.capsdconfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.ConfigFileConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>DefaultCapsdConfigManager class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class DefaultCapsdConfigManager extends CapsdConfigManager implements CapsdConfig {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCapsdConfigManager.class);
    /**
     * Timestamp of the file for the currently loaded configuration
     */
    private long m_currentVersion = -1L;

    /**
     * <p>Constructor for DefaultCapsdConfigManager.</p>
     */
    public DefaultCapsdConfigManager() {
        super();
    }

    /**
     * <p>Constructor for DefaultCapsdConfigManager.</p>
     *
     * @param is a {@link java.io.InputStream} object.
     * @throws org.exolab.castor.xml.MarshalException    if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     */
    public DefaultCapsdConfigManager(InputStream is) throws MarshalException, ValidationException {
        super(is);
    }

    /**
     * <p>update</p>
     *
     * @throws java.io.IOException                       if any.
     * @throws java.io.FileNotFoundException             if any.
     * @throws org.exolab.castor.xml.MarshalException    if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     */
    @Override
    public synchronized void update() throws IOException, FileNotFoundException, MarshalException, ValidationException {
        File configFile = ConfigFileConstants.getFile(ConfigFileConstants.CAPSD_CONFIG_FILE_NAME);

        LOG.debug("Checking to see if capsd configuration should be reloaded from {}", configFile);

        if (m_currentVersion < configFile.lastModified()) {
            LOG.debug("Reloading capsd configuration file");

            long lastModified = configFile.lastModified();

            InputStream is = null;
            try {
                is = new FileInputStream(configFile);
                loadXml(is);
            } finally {
                if (is != null) {
                    IOUtils.closeQuietly(is);
                }
            }

            // Update currentVersion after we have successfully reloaded
            m_currentVersion = lastModified;

            LOG.info("Reloaded capsd configuration file");
        }
    }



    /**
     * {@inheritDoc}
     */
    @Override
    protected synchronized void saveXml(String xml) throws IOException {
        if (xml != null) {
            File cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.CAPSD_CONFIG_FILE_NAME);
            Writer fileWriter = new OutputStreamWriter(new FileOutputStream(cfgFile), "UTF-8");
            fileWriter.write(xml);
            fileWriter.flush();
            fileWriter.close();
        }
    }




}
