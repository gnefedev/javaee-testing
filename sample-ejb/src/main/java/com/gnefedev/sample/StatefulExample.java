package com.gnefedev.sample;

import javax.ejb.Stateful;

/**
 * Created by gerakln on 27.08.16.
 */
@Stateful
public class StatefulExample {
    private int callCount = 0;

    public void increment() {
        callCount++;
    }
    public int getCallCount() {
        return callCount;
    }
}
