package org.opennms.ng.services.trapd;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.opennms.netmgt.model.events.EventIpcManager;
import org.opennms.netmgt.snmp.TrapNotification;
import org.opennms.ng.services.eventconfig.EventConfDao;

import javax.annotation.Resource;

/**
 * This factory constructs {@link TrapQueueProcessor} instances.
 */
public class TrapQueueProcessorFactory {

    /**
     * Whether or not a newSuspect event should be generated with a trap from an
     * unknown IP address
     */
    @Resource(name = "newSuspectOnTrap")
    private Boolean newSuspectOnTrap;

    /**
     * @return the newSuspectOnTrap
     */
    public Boolean getNewSuspect() {
        return newSuspectOnTrap;
    }

    /**
     * @param newSuspectOnTrap the newSuspectOnTrap to set
     */
    public void setNewSuspect(Boolean newSuspectOnTrap) {
        this.newSuspectOnTrap = newSuspectOnTrap;
    }

    /**
     * The event configuration DAO that we use to convert from traps to events.
     */
    private EventConfDao eventConfDao;

    private ProducerTemplate template;

    private String destinationURI;

    private CamelContext camelContext;

    /**
     * The constructor
     */
    public TrapQueueProcessorFactory() {
        template = camelContext.createProducerTemplate();
    }

    public TrapQueueProcessor getInstance(TrapNotification info) {
        TrapQueueProcessor retval = new TrapQueueProcessor();
        retval.setEventConfDao(eventConfDao);
        retval.setDestinationURI(destinationURI);
        retval.setTemplate(template);
        retval.setNewSuspect(newSuspectOnTrap);
        retval.setTrapNotification(info);
        return retval;
    }

    public void setNewSuspectOnTrap(Boolean newSuspectOnTrap) {
        this.newSuspectOnTrap = newSuspectOnTrap;
    }

    public void setEventConfDao(EventConfDao eventConfDao) {
        this.eventConfDao = eventConfDao;
    }

    public void setDestinationURI(String destinationURI) {
        this.destinationURI = destinationURI;
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }
}
