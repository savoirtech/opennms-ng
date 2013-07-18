package org.opennms.ng.testing.jms;

import javax.jms.ConnectionFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;


import static org.apache.camel.component.jms.JmsComponent.jmsComponentAutoAcknowledge;

public class BaseJMSTest extends CamelTestSupport {

    MockEndpoint endpoint;
    //Use this to define/extend your testcase base.

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        endpoint = getMockEndpoint("mock:result");
    }

    @Test
    public void testEmptyBase() {

    }

    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        ConnectionFactory connectionFactory = CamelJmsTestHelper.createConnectionFactory();
        camelContext.addComponent("jms", jmsComponentAutoAcknowledge(connectionFactory));
        return camelContext;
    }






}
