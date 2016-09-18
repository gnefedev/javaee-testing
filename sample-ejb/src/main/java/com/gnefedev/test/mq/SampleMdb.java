package com.gnefedev.test.mq;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;

/**
 * Created by gerakln on 17.09.16.
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destinationType",
                propertyValue = "javax.jms.Queue"
        ),
        @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "jms/queue/requestQueue"
        ),
})
public class SampleMdb implements MessageListener {
    @Resource
    private ConnectionFactory connectionFactory;
    @Resource(name = "jms/queue/responseQueue")
    private Destination responseQueue;

    @Override
    public void onMessage(Message message) {
        try {
            String body = ((TextMessage) message).getText();
            try (Connection con = connectionFactory.createConnection() ) {
                try (Session session = con.createSession(true, Session.AUTO_ACKNOWLEDGE)) {
                    TextMessage response = session.createTextMessage(body + "!!!");
                    try (MessageProducer producer = session.createProducer(responseQueue)) {
                        producer.send(response);
                        System.out.println(response.getText() + " was send to " + responseQueue);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
