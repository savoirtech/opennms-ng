package org.opennms.ng.services.ipc;

import java.util.Collection;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.opennms.netmgt.model.events.EventListener;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Log;

public class EventListenerRouteBuilder extends RouteBuilder {

    String routeId;
    EventListener eventListener;
    Collection<String> strings;

    public EventListenerRouteBuilder(String routeId, EventListener eventListener, Collection<String> strings) {
        this.routeId = routeId;
        this.eventListener = eventListener;
        this.strings = strings;
    }

    @Override
    public void configure() throws Exception {

        if (strings == null || strings.isEmpty()) {
            //All events.
            from(IPCConstants.EVENTBROADCAST).process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    eventListener.onEvent((Event) exchange.getIn().getBody(Event.class));
                }
            });
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(IPCConstants.EVENTBROADCAST);
            boolean first = true;
            for (String uei : strings) {
                if (first) {
                    builder.append("?selector=");
                    builder.append(IPCConstants.UEI + "=");
                    builder.append("'" + uei + "'");
                    first = false;
                } else {
                    builder.append(" OR ");
                    builder.append(IPCConstants.UEI + "=");
                    builder.append("'" + uei + "'");
                }
            }

            System.out.println("Adding eventlistener for " + builder.toString());
            from(builder.toString()).process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    System.out.println("Received exchange " + exchange.getIn().getBody());

                    Object message = exchange.getIn().getBody();

                    if (message instanceof Event) {
                        eventListener.onEvent((Event) message);
                    }

                    if (message instanceof Log) {
                        Log logs = (Log) message;
                        for (Event event : logs.getEvents().getEventCollection()) {
                            eventListener.onEvent(event);
                        }
                    }
                }
            });
        }
    }
}
