package org.opennms.ng.persistence;

import org.junit.Test;
import org.opennms.ng.persistence.dao.OnmsIpInterfaceDao;


import static org.junit.Assert.assertNotNull;

public class IpInterfaceDaoTest extends AbstractTestBase {

    @Test
    public void testWiring() throws Exception {

        OnmsIpInterfaceDao dao = (OnmsIpInterfaceDao) getBean("onmsIpInterfaceDaoJPA");

        assertNotNull(dao);
    }
}
