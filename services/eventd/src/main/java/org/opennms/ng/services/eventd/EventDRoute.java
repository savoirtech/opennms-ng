package org.opennms.ng.services.eventd;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class EventDRoute extends RouteBuilder {

    private String location;
    private Processor processor;

    @Override
    public void configure() throws Exception {
        from(location).process(processor);
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }
}
