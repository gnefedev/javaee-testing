package com.gnefedev.sample;

import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by gerakln on 31.07.16.
 */
@MessageDriven
public class HelloWorld implements MessageListener {
    @Inject
    private FromCore fromCore;

    public void onMessage(Message message) {

    }
}
