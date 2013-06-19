package org.opennms.ng.services.trapd;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

public class TrapdConsumer extends DefaultConsumer{

    private final TrapdEndpoint endpoint;

    public TrapdConsumer(Endpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = (TrapdEndpoint) endpoint;
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
