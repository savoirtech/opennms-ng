package org.opennms.ng.persistence;

import org.opennms.ng.persistence.service.VersionService;
import org.junit.Test;
import static org.junit.Assert.*;

public class VersionTest extends AbstractTestBase {

    @Test
    public void testVersion() throws Exception {

        VersionService svc = (VersionService) getBean("versionSvc");

        String ver = svc.getVersionNumber();
        assertNotNull(ver);

        assertEquals("1.0       ", ver);
    }
}
