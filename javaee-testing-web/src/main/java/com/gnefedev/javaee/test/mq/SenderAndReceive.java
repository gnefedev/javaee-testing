package com.gnefedev.javaee.test.mq;

import com.gnefedev.jee.testing.junit.JavaeeTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.jms.*;
import javax.naming.NamingException;
import javax.transaction.Transactional;
import java.util.Enumeration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by gerakln on 17.09.16.
 */
@RequestScoped
@Stateful
@RunWith(JavaeeTestRunner.class)
public class SenderAndReceive {
    @Resource
    private ConnectionFactory connectionFactory;
    @Resource(name = "jms/queue/requestQueue")
    private Queue requestQueue;
    @Resource(name = "jms/queue/responseQueue")
    private Queue responseQueue;

    @Before
    public void purge() throws JMSException {
        try (Connection con = connectionFactory.createConnection()) {
            try (Session session = con.createSession(true, Session.AUTO_ACKNOWLEDGE)) {
                try (QueueBrowser browser = session.createBrowser(responseQueue)) {
                    Enumeration messages = browser.getEnumeration();
                    while (messages.hasMoreElements()) {
                        Message message = (Message) messages.nextElement();
                        con.start();
                        String selector = "JMSMessageID='" + message.getJMSMessageID() + "'";
                        try (MessageConsumer consumer = session.createConsumer(responseQueue, selector)) {
                            consumer.receive(1_000);
                        }
                    }
                }
            }
        }
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    @Test
    public void sendAndReceive() throws JMSException, NamingException {
        assertNotNull(connectionFactory);
        assertNotNull(requestQueue);
        try (Connection con = connectionFactory.createConnection()) {
            try (Session session = con.createSession()) {
                TextMessage request = session.createTextMessage("Hello, world");
                try (MessageProducer producer = session.createProducer(requestQueue)) {
                    producer.send(request);
                }
            }
        }
        try (Connection con = connectionFactory.createConnection()) {
            try (Session session = con.createSession()) {
                try (MessageConsumer consumer = session.createConsumer(responseQueue)) {
                    con.start();
                    TextMessage response = (TextMessage) consumer.receive(1_000);
                    assertNotNull(response);
                    assertEquals("Hello, world!!!", response.getText());
                }
            }
        }
    }
}
