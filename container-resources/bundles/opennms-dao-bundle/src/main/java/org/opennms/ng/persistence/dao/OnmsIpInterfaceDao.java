package org.opennms.ng.persistence.dao;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import org.opennms.ng.persistence.entities.OnmsIpInterface;
import org.opennms.ng.persistence.entities.OnmsNode;

public interface OnmsIpInterfaceDao extends GenericDao<OnmsIpInterface, Integer> {

    /**
     * <p>get</p>
     *
     * @param node      a {@link OnmsNode} object.
     * @param ipAddress a {@link java.lang.String} object.
     * @return a {@link OnmsIpInterface} object.
     */
    OnmsIpInterface get(OnmsNode node, String ipAddress);

    /**
     * <p>findByNodeIdAndIpAddress</p>
     *
     * @param nodeId    a {@link java.lang.Integer} object.
     * @param ipAddress a {@link java.lang.String} object.
     * @return a {@link OnmsIpInterface} object.
     */
    OnmsIpInterface findByNodeIdAndIpAddress(Integer nodeId, String ipAddress);

    /**
     * <p>findByForeignKeyAndIpAddress</p>
     *
     * @param foreignSource a {@link java.lang.String} object.
     * @param foreignId     a {@link java.lang.String} object.
     * @param ipAddress     a {@link java.lang.String} object.
     * @return a {@link OnmsIpInterface} object.
     */
    OnmsIpInterface findByForeignKeyAndIpAddress(String foreignSource, String foreignId, String ipAddress);

    /**
     * <p>findByIpAddress</p>
     *
     * @param ipAddress a {@link java.lang.String} object.
     * @return a {@link java.util.Collection} object.
     */
    List<OnmsIpInterface> findByIpAddress(String ipAddress);

    /**
     * <p>findByNodeId</p>
     *
     * @param nodeId a {@link java.lang.Integer} object.
     * @return a {@link java.util.Collection} object.
     */
    List<OnmsIpInterface> findByNodeId(Integer nodeId);

    /**
     * <p>findByServiceType</p>
     *
     * @param svcName a {@link java.lang.String} object.
     * @return a {@link java.util.Collection} object.
     */
    List<OnmsIpInterface> findByServiceType(String svcName);

    /**
     * <p>findHierarchyByServiceType</p>
     *
     * @param svcName a {@link java.lang.String} object.
     * @return a {@link java.util.Collection} object.
     */
    List<OnmsIpInterface> findHierarchyByServiceType(String svcName);

    /**
     * Returns a map of all IP to node ID mappings in the database.
     *
     * @return a {@link java.util.Map} object.
     */
    Map<InetAddress, Integer> getInterfacesForNodes();

    OnmsIpInterface findPrimaryInterfaceByNodeId(Integer nodeId);
}
