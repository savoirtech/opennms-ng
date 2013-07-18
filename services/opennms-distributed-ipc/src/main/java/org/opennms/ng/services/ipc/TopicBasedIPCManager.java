package org.opennms.ng.services.ipc;

import java.util.Collection;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolUtils;
import org.apache.commons.pool.impl.StackObjectPool;
import org.opennms.netmgt.model.events.EventIpcManager;
import org.opennms.netmgt.model.events.EventListener;
import org.opennms.netmgt.model.events.EventProxyException;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Log;

public class TopicBasedIPCManager implements EventIpcManager {

    private CamelContext camelContext;
    private ObjectPool producers;

    public TopicBasedIPCManager(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public void sendNow(Event event) {

        ProducerTemplate template=null;
        try {
            template = (ProducerTemplate) producers.borrowObject();
            template.sendBodyAndHeader(IPCConstants.EVENTBROADCAST, event, IPCConstants.UEI, event.getUei());
        } catch (Exception e) {
            e.printStackTrace();  //TODO
        } finally {
            if (template!=null){
                try {
                    producers.returnObject(template);
                } catch (Exception e) {
                    e.printStackTrace();  //TODO
                }
            }
        }
    }

    @Override
    public void sendNow(Log log) {
        //TODO
    }

    @Override
    public void send(Event event) throws EventProxyException {
        sendNow(event);
    }

    @Override
    public void send(Log log) throws EventProxyException {
        //TODO
    }

    @Override
    public void addEventListener(EventListener eventListener) {

        //TODO
    }

    @Override
    public void addEventListener(EventListener eventListener, Collection<String> strings) {

        EventListenerRouteBuilder routeBuilder = new EventListenerRouteBuilder(eventListener.getName(), eventListener, strings);
        try {
            camelContext.addRoutes(routeBuilder);
            camelContext.startRoute(routeBuilder.routeId);
        } catch (Exception e) {
            e.printStackTrace();  //TODO
        }
    }

    @Override
    public void addEventListener(EventListener eventListener, String s) {
        //TODO
    }

    @Override
    public void removeEventListener(EventListener eventListener) {
        //TODO
    }

    @Override
    public void removeEventListener(EventListener eventListener, Collection<String> strings) {
        //TODO
    }

    @Override
    public void removeEventListener(EventListener eventListener, String s) {
        //TODO
    }

    public void init() {
        producers = PoolUtils.erodingPool(new StackObjectPool(new ContextProducerPool(camelContext)));
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }
}
