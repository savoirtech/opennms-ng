package org.opennms.ng.testing.eventd;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.opennms.netmgt.model.events.EventUtils;
import org.opennms.netmgt.xml.event.Events;
import org.opennms.netmgt.xml.event.Header;
import org.opennms.netmgt.xml.event.Log;

public class EventDTester extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("jetty:http://0.0.0.0:10999/event").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Log log = new Log();
                Events events = new Events();

                for(int i=0;i<1000;i++) {
                    events.addEvent(EventUtils.createNodeAddedEvent("SAVOIR", 1, "LABEL", "BOB"));
                }


                Header header = new Header();
                header.setCreated("NOW");

                log.setHeader(header);
                log.setEvents(events);




                exchange.getIn().setBody(log);
            }
        }).to("jms:eventd");
    }
}
