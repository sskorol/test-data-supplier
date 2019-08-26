package io.github.sskorol.core;

import io.github.sskorol.model.DataSupplierMetaData;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.internal.annotations.IDataProvidable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;

import static io.github.sskorol.utils.ReflectionUtils.getDataSupplierAnnotation;
import static io.github.sskorol.utils.ReflectionUtils.getDataSupplierClass;
import static java.util.Objects.nonNull;

/**
 * Core listener which transforms custom DataSupplier format to common TestNG DataProvider.
 * ToDo: Replace IAnnotationTransformer3 with IAnnotationTransformer when #2138 is fixed
 */
public class DataProviderTransformer implements IAnnotationTransformer3 {

    @DataProvider
    public Iterator<Object[]> supplySeqData(final ITestContext context, final ITestNGMethod testMethod) {
        return getMetaData(context, testMethod).getTestData().iterator();
    }

    @DataProvider(parallel = true)
    public Iterator<Object[]> supplyParallelData(final ITestContext context, final ITestNGMethod testMethod) {
        return getMetaData(context, testMethod).getTestData().iterator();
    }

    @Override
    public void transform(final ITestAnnotation annotation, final Class testClass,
                          final Constructor testConstructor, final Method testMethod) {
        assignCustomDataSupplier(annotation, testMethod, testClass);
    }

    @Override
    public void transform(final IFactoryAnnotation annotation, final Method testMethod) {
        assignCustomDataSupplier(annotation, testMethod, null);
    }

    private DataSupplierMetaData getMetaData(final ITestContext context, final ITestNGMethod testMethod) {
        return new DataSupplierMetaData(context, testMethod);
    }

    @SuppressWarnings("FinalLocalVariable")
    private void assignCustomDataSupplier(final IDataProvidable annotation, final Method testMethod,
                                          final Class testClass) {
        var dataSupplierClass = getDataSupplierClass(annotation, testClass, testMethod);
        var dataSupplierAnnotation = getDataSupplierAnnotation(dataSupplierClass, annotation.getDataProvider());

        if (!annotation.getDataProvider().isEmpty() && nonNull(dataSupplierAnnotation)) {
            annotation.setDataProviderClass(getClass());
            annotation.setDataProvider(dataSupplierAnnotation.runInParallel() ? "supplyParallelData" : "supplySeqData");
        }
    }
}
