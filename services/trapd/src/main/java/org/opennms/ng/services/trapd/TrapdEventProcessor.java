package org.opennms.ng.services.trapd;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.snmp.TrapNotification;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.eventconf.Logmsg;
import org.opennms.ng.services.eventconfig.EventConfDao;

import java.net.InetAddress;

import static org.opennms.core.utils.InetAddressUtils.addr;

public class TrapdEventProcessor implements Processor {

    private static final String LOCALHOST_ADDRESS = InetAddressUtils.getLocalHostName();

    private EventConfDao eventConfDao;
    private Boolean newSuspectOnTrap;
    private CamelContext camelContext;
    private ProducerTemplate template;
    private String destinationURI;

    public void init(){
        template = camelContext.createProducerTemplate();
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public void setDestinationURI(String destinationURI) {
        this.destinationURI = destinationURI;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        TrapNotification notification = (TrapNotification) exchange.getIn().getBody();

        processNotification(notification);
    }

    public void setEventConfDao(EventConfDao eventConfDao) {
        this.eventConfDao = eventConfDao;
    }

    public void setNewSuspectOnTrap(Boolean newSuspectOnTrap) {
        this.newSuspectOnTrap = newSuspectOnTrap;
    }

    public void processNotification(TrapNotification notification) {
        try {
            processTrapEvent(((EventCreator)notification.getTrapProcessor()).getEvent());
        } catch (IllegalArgumentException e) {
            log().info(e.getMessage());
        } catch (Throwable e) {
            log().error("Unexpected error processing trap: " + e, e);
        }
    }

    /**
     * <p>processTrapEvent</p>
     *
     * @param event a {@link org.opennms.netmgt.xml.event.Event} object.
     */
    private void processTrapEvent(final Event event) {
        final InetAddress trapInterface = event.getInterfaceAddress();

        final org.opennms.netmgt.xml.eventconf.Event econf = eventConfDao.findByEvent(event);
        if (econf == null || econf.getUei() == null) {
            event.setUei("uei.opennms.org/default/trap");
        } else {
            event.setUei(econf.getUei());
        }

        if (econf != null) {
            final Logmsg logmsg = econf.getLogmsg();
            if (logmsg != null) {
                final String dest = logmsg.getDest();
                if ("discardtraps".equals(dest)) {
                    log().debug("Trap discarded due to matching event having logmsg dest == discardtraps");
                    return;
                }
            }
        }

        // send the event to eventd
        template.sendBody(destinationURI, event);

        log().debug("Trap successfully converted and sent to eventd with UEI " + event.getUei());

        if (!event.hasNodeid() && newSuspectOnTrap) {
            sendNewSuspectEvent(InetAddressUtils.str(trapInterface));

            if (log().isDebugEnabled()) {
                log().debug("Sent newSuspectEvent for interface: " + trapInterface);
            }
        }
    }

    /**
     * Send a newSuspect event for the interface
     *
     * @param trapInterface
     *            The interface for which the newSuspect event is to be
     *            generated
     */
    private void sendNewSuspectEvent(String trapInterface) {
        // construct event with 'trapd' as source
        EventBuilder bldr = new EventBuilder(org.opennms.netmgt.EventConstants.NEW_SUSPECT_INTERFACE_EVENT_UEI, "trapd");
        bldr.setInterface(addr(trapInterface));
        bldr.setHost(LOCALHOST_ADDRESS);

        // send the event to eventd
        template.sendBody(destinationURI, bldr.getEvent());
    }

    private ThreadCategory log() {
        return ThreadCategory.getInstance(getClass());
    }
}
