package io.github.sskorol.core;

import org.testng.annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class IAnnotationTransformerInterceptorImpl implements IAnnotationTransformerInterceptor {

    private final AtomicInteger callsAmount = new AtomicInteger(0);

    @Override
    public void transform(final IFactoryAnnotation annotation, final Method testMethod) {
        // not implemented
    }

    @Override
    public void transform(final IConfigurationAnnotation annotation, final Class testClass,
                          final Constructor testConstructor, final Method testMethod) {
        // not implemented
    }

    @Override
    public void transform(final IDataProviderAnnotation annotation, final Method method) {
        // not implemented
    }

    @Override
    public void transform(final IListenersAnnotation annotation, final Class testClass) {
        // not implemented
    }

    @Override
    public void transform(final ITestAnnotation annotation, final Class testClass,
                          final Constructor testConstructor, final Method testMethod) {
        callsAmount.incrementAndGet();
    }

    public int getTotalTransformerCalls() {
        return callsAmount.intValue();
    }
}
