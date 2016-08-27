package com.gnefedev.sample;

import javax.ejb.MessageDriven;
import javax.inject.Inject;

/**
 * Created by gerakln on 31.07.16.
 */
@MessageDriven
public class HelloWorld {
    @Inject
    private FromCore fromCore;
}
