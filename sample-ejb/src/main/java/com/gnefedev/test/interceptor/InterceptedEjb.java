package com.gnefedev.test.interceptor;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

/**
 * Created by gerakln on 04.09.16.
 */
@Stateless
@Interceptors(CounterInterceptor.class)
public class InterceptedEjb {
    public int countReplacedByInterceptor() {
        return 0;
    }

    public void callToCount() {}
}
