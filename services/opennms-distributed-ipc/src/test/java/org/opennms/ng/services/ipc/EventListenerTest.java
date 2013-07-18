package org.opennms.ng.services.ipc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.opennms.netmgt.model.events.EventListener;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.ng.testing.jms.BaseJMSTest;

public class EventListenerTest extends BaseJMSTest {

    final static String DESCRIPTION = "SMARTYPANTS";
    final static String UEI1 = "net/ding/dong";
    final static String UEI2 = "net/ding/pong";

    @Test
    public void testIPCManager() throws InterruptedException {

        //Context assumes that camel JMS is there for us.
        TopicBasedIPCManager topicBasedIPCManager = new TopicBasedIPCManager(context);
        topicBasedIPCManager.init();
        Event testEvent = new Event();
        testEvent.setDescr(DESCRIPTION);
        testEvent.setUei("net/ding/dong");
        List<String> ueis = new ArrayList<String>();
        ueis.add(UEI1);
        ueis.add(UEI2);

        CountDownLatch latch = new CountDownLatch(3);

        DummyListener listener = new DummyListener(latch);
        topicBasedIPCManager.addEventListener(listener, ueis);

        template.sendBodyAndHeader(IPCConstants.EVENTBROADCAST, testEvent, "uei", "WANG");
        template.sendBodyAndHeader(IPCConstants.EVENTBROADCAST, testEvent, "uei", UEI1);
        template.sendBodyAndHeader(IPCConstants.EVENTBROADCAST, testEvent, "uei", UEI2);

        //Last event #3

        topicBasedIPCManager.sendNow(testEvent);

        latch.await(50000l, TimeUnit.MILLISECONDS);

        topicBasedIPCManager.removeEventListener(listener);
    }

    private class DummyListener implements EventListener {

        private CountDownLatch latch;

        private DummyListener(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public String getName() {
            return "DUMMY";
        }

        @Override
        public void onEvent(Event event) {
            System.out.println("Event received " + event);
            assertTrue(EventListenerTest.DESCRIPTION.equals(event.getDescr()));
            latch.countDown();
        }
    }
}
