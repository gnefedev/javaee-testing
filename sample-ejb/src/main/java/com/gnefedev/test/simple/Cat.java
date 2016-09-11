package com.gnefedev.test.simple;

import javax.ejb.Stateful;

/**
 * Created by gerakln on 11.09.16.
 */
@Stateful(name = "cat")
public class Cat implements Animal {
    @Override
    public String sound() {
        return "meaou";
    }
}
