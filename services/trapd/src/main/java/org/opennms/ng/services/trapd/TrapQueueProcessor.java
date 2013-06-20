package org.opennms.ng.services.trapd;

import org.apache.camel.ProducerTemplate;
import org.opennms.core.concurrent.WaterfallCallable;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.events.EventIpcManager;
import org.opennms.netmgt.snmp.TrapNotification;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.eventconf.Logmsg;
import org.opennms.ng.services.eventconfig.EventConfDao;

import java.net.InetAddress;
import java.util.concurrent.Callable;

import static org.opennms.core.utils.InetAddressUtils.addr;

/**
 * The TrapQueueProcessor handles the conversion of V1 and V2 traps to events
 * and sending them out the JSDT channel that eventd is listening on
 *
 * @author <A HREF="mailto:weave@oculan.com">Brian Weaver </A>
 * @author <A HREF="mailto:sowmya@opennms.org">Sowmya Nataraj </A>
 * @author <A HREF="mailto:larry@opennms.org">Lawrence Karnowski </A>
 * @author <A HREF="mailto:mike@opennms.org">Mike Davidson </A>
 * @author <A HREF="mailto:tarus@opennms.org">Tarus Balog </A>
 * @author <A HREF="http://www.opennms.org">OpenNMS.org </A>
 */
class TrapQueueProcessor implements WaterfallCallable {
    /**
     * The name of the local host.
     */
    private static final String LOCALHOST_ADDRESS = InetAddressUtils.getLocalHostName();

    /**
     * Whether or not a newSuspect event should be generated with a trap from an
     * unknown IP address
     */
    private Boolean newSuspect;

    /**
     * The event configuration DAO that we use to convert from traps to events.
     */
    private EventConfDao eventConfDao;

    private TrapNotification trapNotification;

    private ProducerTemplate template;

    private String destinationURI;

    /**
     * Process a V2 trap and convert it to an event for transmission.
     * <p/>
     * <p>
     * From RFC2089 ('Mapping SNMPv2 onto SNMPv1'), section 3.3 ('Processing an
     * outgoing SNMPv2 TRAP')
     * </p>
     * <p/>
     * <p>
     * <strong>2b </strong>
     * <p>
     * If the snmpTrapOID.0 value is one of the standard traps the specific-trap
     * field is set to zero and the generic trap field is set according to this
     * mapping:
     * <p>
     * <p/>
     * <pre>
     *
     *
     *
     *
     *
     *
     *            value of snmpTrapOID.0                generic-trap
     *            ===============================       ============
     *            1.3.6.1.6.3.1.1.5.1 (coldStart)                  0
     *            1.3.6.1.6.3.1.1.5.2 (warmStart)                  1
     *            1.3.6.1.6.3.1.1.5.3 (linkDown)                   2
     *            1.3.6.1.6.3.1.1.5.4 (linkUp)                     3
     *            1.3.6.1.6.3.1.1.5.5 (authenticationFailure)      4
     *            1.3.6.1.6.3.1.1.5.6 (egpNeighborLoss)            5
     *
     *
     *
     *
     *
     *
     * </pre>
     * <p/>
     * <p>
     * The enterprise field is set to the value of snmpTrapEnterprise.0 if this
     * varBind is present, otherwise it is set to the value snmpTraps as defined
     * in RFC1907 [4].
     * </p>
     * <p/>
     * <p>
     * <strong>2c. </strong>
     * </p>
     * <p>
     * If the snmpTrapOID.0 value is not one of the standard traps, then the
     * generic-trap field is set to 6 and the specific-trap field is set to the
     * last subid of the snmpTrapOID.0 value.
     * </p>
     * <p/>
     * <p>
     * If the next to last subid of snmpTrapOID.0 is zero, then the enterprise
     * field is set to snmpTrapOID.0 value and the last 2 subids are truncated
     * from that value. If the next to last subid of snmpTrapOID.0 is not zero,
     * then the enterprise field is set to snmpTrapOID.0 value and the last 1
     * subid is truncated from that value.
     * </p>
     * <p/>
     * <p>
     * In any event, the snmpTrapEnterprise.0 varBind (if present) is ignored in
     * this case.
     * </p>
     */
    @Override
    public Callable<Void> call() {
        try {
            processTrapEvent(((EventCreator) trapNotification.getTrapProcessor()).getEvent());
        } catch (IllegalArgumentException e) {
            log().info(e.getMessage());
        } catch (Throwable e) {
            log().error("Unexpected error processing trap: " + e, e);
        }
        return null;
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

        if (!event.hasNodeid() && newSuspect) {
            sendNewSuspectEvent(InetAddressUtils.str(trapInterface));

            if (log().isDebugEnabled()) {
                log().debug("Sent newSuspectEvent for interface: " + trapInterface);
            }
        }
    }

    /**
     * Send a newSuspect event for the interface
     *
     * @param trapInterface The interface for which the newSuspect event is to be
     *                      generated
     */
    private void sendNewSuspectEvent(String trapInterface) {
        // construct event with 'trapd' as source
        EventBuilder bldr = new EventBuilder(org.opennms.netmgt.EventConstants.NEW_SUSPECT_INTERFACE_EVENT_UEI, "trapd");
        bldr.setInterface(addr(trapInterface));
        bldr.setHost(LOCALHOST_ADDRESS);

        // send the event to eventd
        template.sendBody(destinationURI, bldr.getEvent());
    }

    /**
     * The constructor
     */
    public TrapQueueProcessor() {
    }

    private ThreadCategory log() {
        return ThreadCategory.getInstance(getClass());
    }

    /**
     * <p>getEventConfDao</p>
     */
    public EventConfDao getEventConfDao() {
        return eventConfDao;
    }

    /**
     * <p>setEventConfDao</p>
     */
    public void setEventConfDao(EventConfDao eventConfDao) {
        this.eventConfDao = eventConfDao;
    }

    /**
     * <p>isNewSuspect</p>
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean isNewSuspect() {
        return newSuspect;
    }

    /**
     * <p>setNewSuspect</p>
     *
     * @param newSuspect a {@link java.lang.Boolean} object.
     */
    public void setNewSuspect(Boolean newSuspect) {
        this.newSuspect = newSuspect;
    }

    public TrapNotification getTrapNotification() {
        return trapNotification;
    }

    public void setTrapNotification(TrapNotification info) {
        trapNotification = info;
    }

    void setTemplate(ProducerTemplate template) {
        this.template = template;
    }

    void setDestinationURI(String destinationURI) {
        this.destinationURI = destinationURI;
    }
}

