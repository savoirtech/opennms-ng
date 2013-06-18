package org.opennms.ng.services.trapd;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

public class TrapdEndpoint extends DefaultEndpoint {
    @Override
    public Producer createProducer() throws Exception {
        throw new UnsupportedOperationException("Producer not supported for trapd endpoint");
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
