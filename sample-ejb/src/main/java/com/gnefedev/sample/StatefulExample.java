package com.gnefedev.sample;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 * Created by gerakln on 27.08.16.
 */
@SessionScoped
@Stateful
public class StatefulExample implements Serializable {
    private int callCount = 0;

    public void increment() {
        callCount++;
    }
    public int getCallCount() {
        return callCount;
    }
}
