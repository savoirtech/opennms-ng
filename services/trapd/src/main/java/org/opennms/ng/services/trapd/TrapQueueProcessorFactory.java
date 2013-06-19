package org.opennms.ng.services.trapd;

//*******************************************************************************

import javax.annotation.Resource;

import org.opennms.core.utils.BeanUtils;
import org.opennms.netmgt.model.events.EventIpcManager;
import org.opennms.netmgt.snmp.TrapNotification;
import org.opennms.ng.services.eventconfig.EventConfDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class TrapQueueProcessorFactory implements InitializingBean {

    /**
     * Whether or not a newSuspect event should be generated with a trap from an
     * unknown IP address
     */
    @Resource(name="newSuspectOnTrap")
    private Boolean m_newSuspectOnTrap;

    /**
     * @return the newSuspectOnTrap
     */
    public Boolean getNewSuspect() {
        return m_newSuspectOnTrap;
    }

    /**
     * @param newSuspectOnTrap the newSuspectOnTrap to set
     */
    public void setNewSuspect(Boolean newSuspectOnTrap) {
        m_newSuspectOnTrap = newSuspectOnTrap;
    }

    /**
     * The event IPC manager to which we send events created from traps.
     */
    private EventIpcManager m_eventManager;

    /**
     * @return the eventMgr
     */
    public EventIpcManager getEventManager() {
        return m_eventManager;
    }

    /**
     * @param eventManager the eventMgr to set
     */
    public void setEventManager(EventIpcManager eventManager) {
        m_eventManager = eventManager;
    }

    /**
     * The event configuration DAO that we use to convert from traps to events.
     */
    @Autowired
    private EventConfDao m_eventConfDao;

    /**
     * The constructor
     */
    public TrapQueueProcessorFactory() {
    }

    public TrapQueueProcessor getInstance(TrapNotification info) {
        TrapQueueProcessor retval = new TrapQueueProcessor();
        retval.setEventConfDao(m_eventConfDao);
        retval.setEventManager(m_eventManager);
        retval.setNewSuspect(m_newSuspectOnTrap);
        retval.setTrapNotification(info);
        retval.afterPropertiesSet();
        return retval;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
        Assert.state(m_eventManager != null, "eventManager must be set");
    }
}

