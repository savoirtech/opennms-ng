package org.opennms.ng.services.ipc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolUtils;
import org.apache.commons.pool.impl.StackObjectPool;
import org.opennms.netmgt.model.events.EventIpcBroadcaster;
import org.opennms.netmgt.model.events.EventIpcManager;
import org.opennms.netmgt.model.events.EventListener;
import org.opennms.netmgt.model.events.EventProxyException;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopicBasedIPCManager implements EventIpcManager, EventIpcBroadcaster {

    private CamelContext camelContext;
    private ObjectPool producers;
    Logger logger = LoggerFactory.getLogger(TopicBasedIPCManager.class);

    public TopicBasedIPCManager(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public void sendNow(Event event) {

        ProducerTemplate template = null;
        try {
            template = (ProducerTemplate) producers.borrowObject();
            template.sendBodyAndHeader(IPCConstants.EVENTBROADCAST, event, IPCConstants.UEI, event.getUei());
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (template != null) {
                try {
                    producers.returnObject(template);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public void sendNow(Log log) {
        for (Event event : log.getEvents().getEventCollection()) {
            sendNow(event);
        }
    }

    @Override
    public void send(Event event) throws EventProxyException {
        sendNow(event);
    }

    @Override
    public void send(Log log) throws EventProxyException {
        sendNow(log);
    }

    @Override
    public void addEventListener(EventListener eventListener) {

        List<String> list = new ArrayList<String>();
        addEventListener(eventListener, list);
    }

    @Override
    public void addEventListener(EventListener eventListener, Collection<String> strings) {

        EventListenerRouteBuilder routeBuilder = new EventListenerRouteBuilder(eventListener.getName(), eventListener, strings);
        try {
            camelContext.addRoutes(routeBuilder);
            camelContext.startRoute(routeBuilder.routeId);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void addEventListener(EventListener eventListener, String s) {
        List<String> list = new ArrayList<String>();
        list.add(s);
        addEventListener(eventListener, list);
    }

    @Override
    public void removeEventListener(EventListener eventListener) {
        try {
            camelContext.removeRoute(eventListener.getName());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void removeEventListener(EventListener eventListener, Collection<String> strings) {
        removeEventListener(eventListener);
    }

    @Override
    public void removeEventListener(EventListener eventListener, String s) {
        removeEventListener(eventListener);
    }

    public void init() {
        producers = PoolUtils.erodingPool(new StackObjectPool(new ContextProducerPool(camelContext)));
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public void broadcastNow(Event event) {
        sendNow(event);
    }
}
