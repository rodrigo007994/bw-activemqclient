/*
 * Copyright (c) 2001-2006 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ObjectMethodsFilter implements InvocationHandler {
    private final Method equalsMethod;

    private final Method hashCodeMethod;

    private final Method toStringMethod;

    private final MockInvocationHandler delegate;

    public ObjectMethodsFilter(MockInvocationHandler delegate) {
        try {
            equalsMethod = Object.class.getMethod("equals",
                    new Class[] { Object.class });
            hashCodeMethod = Object.class.getMethod("hashCode", (Class[]) null);
            toStringMethod = Object.class.getMethod("toString", (Class[]) null);
        } catch (NoSuchMethodException e) {
            // ///CLOVER:OFF
            throw new RuntimeException("An Object method could not be found!");
            // ///CLOVER:ON
        }
        this.delegate = delegate;
    }

    public final Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (equalsMethod.equals(method)) {
            return proxy == args[0] ? Boolean.TRUE : Boolean.FALSE;
        }
        if (hashCodeMethod.equals(method)) {
            return new Integer(System.identityHashCode(proxy));
        }
        if (toStringMethod.equals(method)) {
            return mockToString(proxy);
        }
        return delegate.invoke(proxy, method, args);
    }

    private String mockToString(Object proxy) {
        return "EasyMock for " + mockType(proxy);
    }

    private String mockType(Object proxy) {
        if (Proxy.isProxyClass(proxy.getClass()))
            return proxy.getClass()
                    .getInterfaces()[0].toString();
        else
            return proxy.getClass()
            .getSuperclass().toString();
    }

    public MockInvocationHandler getDelegate() {
        return delegate;
    }
}