package org.opennms.ng.services.trapd;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.impl.DefaultExchange;
import org.opennms.netmgt.snmp.TrapNotification;

public class TrapdConsumer extends DefaultConsumer {

    private final TrapdEndpoint endpoint;
    private final Processor processor;
    private Trapd trapd;

    public TrapdConsumer(Endpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = (TrapdEndpoint) endpoint;
        this.processor = processor;

        TrapdConfiguration config = ((TrapdEndpoint) endpoint).getConfig();

        trapd = new Trapd(this);
        trapd.setTrapdIpMgr(config.getTrapdIpMgr());
        trapd.setSnmpTrapAddress(config.getSnmpTrapAddress());
        trapd.setSnmpTrapPort(config.getSnmpTrapPort());
        trapd.setSnmpV3Users(config.getSnmpV3Users());

        trapd.onInit();
    }

    public void onTrap(TrapNotification trapNotification) throws Exception {

        Exchange exchange = new DefaultExchange(endpoint);
        exchange.getIn().setBody(trapNotification);

        processor.process(exchange);
    }

    @Override
    protected void doStop() throws Exception {
        trapd.onStop();
        super.doStop();
    }

    @Override
    protected void doStart() throws Exception {
        trapd.onStart();
        super.doStart();
    }
}
