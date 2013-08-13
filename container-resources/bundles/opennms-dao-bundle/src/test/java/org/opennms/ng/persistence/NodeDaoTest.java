package org.opennms.ng.persistence;

import org.junit.Test;
import org.opennms.ng.persistence.dao.OnmsNodeDao;
import org.opennms.ng.persistence.entities.OnmsNode;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class NodeDaoTest extends AbstractTestBase {

    @Test
    public void testWiring() throws Exception {

        OnmsNodeDao dao = (OnmsNodeDao) getBean("onmsNodeDaoJPA");

        assertNotNull(dao);

        OnmsNode node= dao.read(0);

        assertNull(node);
    }
}
