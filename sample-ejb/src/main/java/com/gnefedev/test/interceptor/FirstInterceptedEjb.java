package com.gnefedev.test.interceptor;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

/**
 * Created by gerakln on 04.09.16.
 */
@Stateless
@Interceptors(CounterInterceptor.class)
public class FirstInterceptedEjb {
    @Interceptors(HitInterceptor.class)
    public void clear() {}

    public int countReplacedByInterceptor() {
        return 0;
    }

    @Interceptors(HitInterceptor.class)
    public void callToCount() {}

    public int sumWithCount(int num) {
        return 0;
    }
}
