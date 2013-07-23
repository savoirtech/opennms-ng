package org.opennms.ng.services.eventd;

import org.opennms.netmgt.model.events.EventIpcManager;
import org.opennms.netmgt.model.events.EventProcessor;
import org.opennms.netmgt.model.events.EventProcessorException;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Header;

public class EventToTopic implements EventProcessor {

    EventIpcManager ipcManager;

    @Override
    public void process(Header header, Event event) throws EventProcessorException {
        //This assumes an event came via the tcp port, we put it on a queue.
        ipcManager.sendNow(event);
    }

    public void setIpcManager(EventIpcManager ipcManager) {
        this.ipcManager = ipcManager;
    }
}
