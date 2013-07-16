package org.opennms.ng.services.passiveconfig;

public class PassiveStatusKey {

    private String m_nodeLabel;
    private String m_ipAddr;
    private String m_serviceName;

    /**
     * <p>Constructor for PassiveStatusKey.</p>
     *
     * @param nodeLabel a {@link java.lang.String} object.
     * @param ipAddr a {@link java.lang.String} object.
     * @param serviceName a {@link java.lang.String} object.
     */
    public PassiveStatusKey(String nodeLabel, String ipAddr, String serviceName) {
        m_nodeLabel = nodeLabel;
        m_ipAddr = ipAddr;
        m_serviceName = serviceName;
    }

    /**
     * <p>getIpAddr</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getIpAddr() {
        return m_ipAddr;
    }

    /**
     * <p>getNodeLabel</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNodeLabel() {
        return m_nodeLabel;
    }

    /**
     * <p>getServiceName</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getServiceName() {
        return m_serviceName;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (o instanceof PassiveStatusKey) {
            PassiveStatusKey key = (PassiveStatusKey) o;
            return getNodeLabel().equals(key.getNodeLabel()) &&
                    getIpAddr().equals(key.getIpAddr()) &&
                    getServiceName().equals(key.getServiceName());
        }
        return false;
    }

    /**
     * <p>hashCode</p>
     *
     * @return a int.
     */
    @Override
    public int hashCode() {
        return getNodeLabel().hashCode() ^ getIpAddr().hashCode() ^ getServiceName().hashCode();
    }

    /**
     * <p>toString</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String toString() {
        return getNodeLabel()+':'+getIpAddr()+':'+getServiceName();
    }


}
