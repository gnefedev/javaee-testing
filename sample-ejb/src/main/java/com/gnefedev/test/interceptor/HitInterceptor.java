package com.gnefedev.test.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Created by gerakln on 05.09.16.
 */
public class HitInterceptor {
    public static boolean wasCalled = false;
    @AroundInvoke
    public Object testing(InvocationContext context) throws Exception {
        String methodName = context.getMethod().getName();
        switch (methodName) {
            case "callToCount":
                wasCalled = true;
                break;
            case "clear":
                wasCalled = false;
                break;
        }
        return context.proceed();
    }
}
