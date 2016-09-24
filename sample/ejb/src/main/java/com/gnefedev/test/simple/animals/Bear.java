package com.gnefedev.test.simple.animals;

import javax.ejb.Stateless;

/**
 * Created by gerakln on 11.09.16.
 */
@Biggest
@Stateless
public class Bear implements Animal {
    @Override
    public String sound() {
        return "gr";
    }
}
