package com.gnefedev.test.simple.animals;

import javax.ejb.Stateless;

/**
 * Created by gerakln on 11.09.16.
 */
@King
@Stateless
public class Lion implements Animal {
    @Override
    public String sound() {
        return "r-r-r";
    }
}
