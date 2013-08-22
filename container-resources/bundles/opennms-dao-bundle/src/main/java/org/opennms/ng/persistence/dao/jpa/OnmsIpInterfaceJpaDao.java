package org.opennms.ng.persistence.dao.jpa;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.events.EventUtils;
import org.opennms.ng.persistence.dao.OnmsIpInterfaceDao;
import org.opennms.ng.persistence.entities.OnmsNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.Query;

public class OnmsIpInterfaceJpaDao extends GenericJpaDao<OnmsIpInterface, Integer> implements OnmsIpInterfaceDao {

    private static final Logger LOG = LoggerFactory.getLogger(OnmsIpInterfaceJpaDao.class);
    String m_findByServiceTypeQuery = null;

    public OnmsIpInterfaceJpaDao() {
        super(OnmsIpInterface.class);
//        m_findByServiceTypeQuery = System.getProperty("org.opennms.dao.ipinterface.findByServiceType",
//                "select distinct ipInterface from OnmsIpInterface as ipInterface join ipInterface.monitoredServices as monSvc where monSvc.serviceType"
//                        + ".name = ?");
        m_findByServiceTypeQuery = System.getProperty("org.opennms.dao.ipinterface.findByServiceType",
            "select distinct ipi from OnmsIpInterface ipi JOIN ipi.monitoredServices monSvc JOIN monSvc.serviceType svcType where svcType.name = ?1");
    }

    /**
     * {@inheritDoc}
     */
    public OnmsIpInterface get(OnmsNode node, String ipAddress) {
        return findUnique("select ipInterface from OnmsIpInterface ipInterface where ipInterface.node = ?1 and ipInterface.ipAddress = ?2", node, InetAddressUtils.getInetAddress(ipAddress));
    }

    /**
     * {@inheritDoc}
     */
    public List<OnmsIpInterface> findByIpAddress(String ipAddress) {
        return find("select ipInterface from OnmsIpInterface ipInterface where ipInterface.ipAddress = ?1",  InetAddressUtils.getInetAddress(ipAddress));
    }

    /**
     * {@inheritDoc}
     */
    public List<OnmsIpInterface> findByNodeId(Integer nodeId) {
        Assert.notNull(nodeId, "nodeId cannot be null");
        return find("select ipInterface from OnmsIpInterface ipInterface where ipInterface.node.id = ?1", nodeId);
    }

    /**
     * {@inheritDoc}
     */
    public OnmsIpInterface findByNodeIdAndIpAddress(Integer nodeId, String ipAddress) {
        InetAddress addr = InetAddressUtils.getInetAddress(ipAddress);
        return findUnique("select ipInterface from OnmsIpInterface as ipInterface join ipInterface.node as node where node.id = ?1 and ipInterface.ipAddress = ?2",
            nodeId, InetAddressUtils.getInetAddress(ipAddress));
    }

    /**
     * {@inheritDoc}
     */
    public OnmsIpInterface findByForeignKeyAndIpAddress(String foreignSource, String foreignId, String ipAddress) {
        return findUnique(
            "select ipInterface from OnmsIpInterface as ipInterface join ipInterface.node as node where node.foreignSource = ?1 and node.foreignId ="
                + " ?2 and ipInterface.ipAddress = ?3", foreignSource, foreignId, InetAddressUtils.getInetAddress(ipAddress));
    }

    /**
     * {@inheritDoc}
     */
    public List<OnmsIpInterface> findByServiceType(String svcName) {

//        Query q = em.createQuery(m_findByServiceTypeQuery);
//        q.setParameter("name", svcName);
//
//        return q.getResultList();

        return find(m_findByServiceTypeQuery, svcName);
    }

    /**
     * {@inheritDoc}
     */
    public List<OnmsIpInterface> findHierarchyByServiceType(String svcName) {
        return find("select distinct ipInterface " +
            "from OnmsIpInterface ipInterface " +
            "left join fetch ipInterface.node node " +
            "left join fetch node.assetRecord " +
            "left join fetch node.snmpInterfaces snmpIf " +
            "left join fetch snmpIf.ipInterfaces " +
            "join ipInterface.monitoredServices monSvc " +
            "where monSvc.serviceType.name = ?1", svcName);
    }

    /**
     * <p>getInterfacesForNodes</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<InetAddress, Integer> getInterfacesForNodes() {
        Map<InetAddress, Integer> map = new HashMap<InetAddress, Integer>();

        // Add all primary addresses first
        @SuppressWarnings("unchecked") List<Object[]> l = find(
            "select distinct ipInterface.ipAddress, ipInterface.node.id from OnmsIpInterface as ipInterface where ipInterface.isSnmpPrimary = 'P'");
        for (Object[] tuple : l) {
            InetAddress ip = (InetAddress) tuple[0];
            Integer nodeId = (Integer) tuple[1];
            map.put(ip, nodeId);
        }

        // Add all non-primary addresses only if those addresses doesn't exist on the map.
        @SuppressWarnings("unchecked") List<Object[]> s = find(
            "select distinct ipInterface.ipAddress, ipInterface.node.id from OnmsIpInterface as ipInterface where ipInterface.isSnmpPrimary != 'P'");
        for (Object[] tuple : s) {
            InetAddress ip = (InetAddress) tuple[0];
            Integer nodeId = (Integer) tuple[1];
            if (!map.containsKey(ip)) {
                map.put(ip, nodeId);
            }
        }

        return map;
    }

    /**
     * <p>addressExistsWithForeignSource</p>
     *
     * @param ipAddress     a {@link java.lang.String} object.
     * @param foreignSource a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean addressExistsWithForeignSource(String ipAddress, String foreignSource) {
        Assert.notNull(ipAddress, "ipAddress cannot be null");
        if (foreignSource == null) {
            return queryInt("select count(ipInterface.id) from OnmsIpInterface as ipInterface " +
                "join ipInterface.node as node " +
                "where node.foreignSource is NULL " +
                "and ipInterface.ipAddress = ? ", ipAddress) > 0;
        } else {
            return queryInt("select count(ipInterface.id) from OnmsIpInterface as ipInterface " +
                "join ipInterface.node as node " +
                "where node.foreignSource = ? " +
                "and ipInterface.ipAddress = ? ", foreignSource, ipAddress) > 0;
        }
    }

    /**
     * This function should be kept similar to {@link OnmsNode#getPrimaryInterface()}.
     */
    public OnmsIpInterface findPrimaryInterfaceByNodeId(final Integer nodeId) {
        Assert.notNull(nodeId, "nodeId cannot be null");
        // SELECT ipaddr FROM ipinterface WHERE nodeid = ? AND issnmpprimary = 'P'

        List<OnmsIpInterface> primaryInterfaces = find(
            "select ipInterface from OnmsIpInterface ipInterface where ipInterface.node.id = ?1 and ipInterface.isSnmpPrimary = 'P' order by ipInterface.ipLastCapsdPoll desc",
            nodeId);
        if (primaryInterfaces.size() < 1) {
            return null;
        } else {
            OnmsIpInterface retval = primaryInterfaces.iterator().next();
            if (primaryInterfaces.size() > 1) {
                LOG.warn("Multiple primary SNMP interfaces for node {}, returning most recently scanned interface: {}", nodeId,
                    retval.getInterfaceId());
            }
            return retval;
        }
    }
}
