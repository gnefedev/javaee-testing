package com.gnefedev.test.simple;

import javax.ejb.Stateless;

/**
 * Created by gerakln on 31.07.16.
 */
@Stateless
public class StatelessExample {
    public String getGreetings() {
        return "Hello, World!!!";
    }
}
