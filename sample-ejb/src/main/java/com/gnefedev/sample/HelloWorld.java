package com.gnefedev.sample;

import javax.ejb.Stateless;

/**
 * Created by gerakln on 31.07.16.
 */
@Stateless
public class HelloWorld {
    public String getGreetings() {
        return "Hello, World!!!";
    }
}
