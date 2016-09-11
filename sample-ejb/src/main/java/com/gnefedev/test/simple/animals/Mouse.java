package com.gnefedev.test.simple.animals;

import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

/**
 * Created by gerakln on 11.09.16.
 */
@King
@Alternative
@Stateless
public class Mouse implements Animal {
    @Override
    public String sound() {
        return "squeak";
    }
}
