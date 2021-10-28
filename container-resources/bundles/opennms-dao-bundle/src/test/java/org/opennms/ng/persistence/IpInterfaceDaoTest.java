package org.opennms.ng.persistence;

import org.junit.Test;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.ng.persistence.dao.OnmsIpInterfaceDao;
import org.opennms.ng.persistence.entities.OnmsNode;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class IpInterfaceDaoTest extends AbstractTestBase {

    @Test
    public void testWiring() throws Exception {

        OnmsIpInterfaceDao dao = (OnmsIpInterfaceDao) getBean("onmsIpInterfaceDaoJPA");
        assertNotNull(dao);

        dao.findByServiceType("test");
        dao.findByNodeIdAndIpAddress(1, "127.0.0.1");

        OnmsIpInterface ipInterface = dao.read(0);
        assertNull(ipInterface);




        OnmsNode node = new OnmsNode();
        node.setCreateTime(new Date());
        node.setLabel("TEST");

        OnmsIpInterface ipi = new OnmsIpInterface(InetAddressUtils.getInetAddress("127.0.0.1"), node);
        ipi.setIsManaged("M");
        ipi.setIpHostName("TEST");
        dao.create(ipi);

        //get
        dao.get(node,"127.0.0.1");

        //findByIpAddress
        dao.findByIpAddress("127.0.0.1");

        //findByNodeId
        dao.findByNodeId(1);

        //findByForeignKeyAndIpAddress
        dao.findByForeignKeyAndIpAddress("1","1","127.0.0.1");

        //findHierarchyByServiceType
        //dao.findHierarchyByServiceType("test");

        //findPrimaryInterfaceByNodeId
        dao.findPrimaryInterfaceByNodeId(1);

    }
}
