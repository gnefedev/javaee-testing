package com.gnefedev.test.simple.animals;

import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

/**
 * Created by gerakln on 11.09.16.
 */
@Alternative
@Biggest
@Stateless
public class Elephant implements Animal {
    @Override
    public String sound() {
        return "ugh";
    }
}
