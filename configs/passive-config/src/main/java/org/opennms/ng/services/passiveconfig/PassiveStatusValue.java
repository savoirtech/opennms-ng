package org.opennms.ng.services.passiveconfig;

import org.opennms.netmgt.model.PollStatus;

/**
 * <p>PassiveStatusValue class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class PassiveStatusValue {

    private PassiveStatusKey m_key;
    private PollStatus m_status;

    /**
     * <p>Constructor for PassiveStatusValue.</p>
     *
     * @param nodeLabel a {@link java.lang.String} object.
     * @param ipAddr a {@link java.lang.String} object.
     * @param serviceName a {@link java.lang.String} object.
     * @param status a {@link org.opennms.netmgt.model.PollStatus} object.
     */
    public PassiveStatusValue(String nodeLabel, String ipAddr, String serviceName, PollStatus status) {
        this(new PassiveStatusKey(nodeLabel, ipAddr, serviceName), status);
    }

    /**
     * <p>Constructor for PassiveStatusValue.</p>
     *
     * @param key a {@link org.opennms.ng.services.passiveconfig.PassiveStatusKey} object.
     * @param status a {@link org.opennms.netmgt.model.PollStatus} object.
     */
    public PassiveStatusValue(PassiveStatusKey key, PollStatus status) {
        m_key = key;
        m_status = status;
    }

    /**
     * <p>getStatus</p>
     *
     * @return a {@link org.opennms.netmgt.model.PollStatus} object.
     */
    public PollStatus getStatus() {
        return m_status;
    }

    /**
     * <p>setStatus</p>
     *
     * @param status a {@link org.opennms.netmgt.model.PollStatus} object.
     */
    public void setStatus(PollStatus status) {
        m_status = status;
    }

    /**
     * <p>getKey</p>
     *
     * @return a {@link org.opennms.ng.services.passiveconfig.PassiveStatusKey} object.
     */
    public PassiveStatusKey getKey() {
        return m_key;
    }

    /**
     * <p>toString</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String toString() {
        return getKey().toString()+" -> "+m_status;
    }


}
