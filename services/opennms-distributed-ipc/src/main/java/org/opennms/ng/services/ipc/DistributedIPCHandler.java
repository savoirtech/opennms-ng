package org.opennms.ng.services.ipc;

import java.util.Collection;

import org.opennms.netmgt.model.events.EventIpcManager;
import org.opennms.netmgt.model.events.EventListener;
import org.opennms.netmgt.model.events.EventProxyException;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Log;

public class DistributedIPCHandler implements EventIpcManager {
    @Override
    public void sendNow(Event event) {
        //TODO
    }

    @Override
    public void sendNow(Log log) {
        //TODO
    }

    @Override
    public void send(Event event) throws EventProxyException {
        //TODO
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
        //TODO
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
}
