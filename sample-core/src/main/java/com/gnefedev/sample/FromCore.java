package com.gnefedev.sample;

import javax.ejb.Singleton;

/**
 * Created by gerakln on 14.08.16.
 */
@Singleton
public class FromCore {
    public String getGreetings() {
        return "Hello, World!!!";
    }
}
