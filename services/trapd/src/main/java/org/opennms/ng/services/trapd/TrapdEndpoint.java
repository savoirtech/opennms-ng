package org.opennms.ng.services.trapd;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

public class TrapdEndpoint extends DefaultEndpoint {

    private TrapdConfiguration config;

    public TrapdEndpoint(String uri, String remaining, TrapdComponent trapdComponent, TrapdConfiguration config) {
        super(uri, trapdComponent);
        this.config = config;
    }

    @Override
    public Producer createProducer() throws Exception {
        throw new UnsupportedOperationException("Producer not supported for trapd endpoint");
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new TrapdConsumer(this, processor);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public TrapdConfiguration getConfig() {
        return config;
    }
}
