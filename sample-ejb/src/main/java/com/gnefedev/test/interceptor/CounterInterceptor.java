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
        switch (methodName) {
            case "callToCount":
                callsCount++;
                break;
            case "countReplacedByInterceptor":
                return callsCount;
            case "clear":
                callsCount = 0;
                break;
        }
        return context.proceed();
    }
}
