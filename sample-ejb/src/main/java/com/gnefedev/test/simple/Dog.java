package com.gnefedev.test.simple;

import javax.ejb.Stateful;

/**
 * Created by gerakln on 11.09.16.
 */
@Stateful(name = "dog")
public class Dog implements Animal {
    @Override
    public String sound() {
        return "bark";
    }
}
