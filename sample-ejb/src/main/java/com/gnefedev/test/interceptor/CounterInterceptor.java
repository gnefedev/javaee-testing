package com.gnefedev.test.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Created by gerakln on 04.09.16.
 */
public class CounterInterceptor {
    private int callsCount = 0;
    @AroundInvoke
    public Object testing(InvocationContext context) throws Exception {
        String methodName = context.getMethod().getName();
        if (methodName.equals("callToCount")) {
            callsCount++;
        } else if (methodName.equals("countReplacedByInterceptor")) {
            return callsCount;
        }
        return context.proceed();
    }
}
