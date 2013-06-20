package org.opennms.ng.services.trapd;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.pool.BasePoolableObjectFactory;

public class ContextProducerPool extends BasePoolableObjectFactory {

    private CamelContext context;

    public ContextProducerPool(CamelContext context) {
        this.context = context;
    }

    @Override
    public Object makeObject() throws Exception {
        ProducerTemplate template = context.createProducerTemplate();
        return template;
    }
}
