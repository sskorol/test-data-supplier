package io.github.sskorol.core;

import io.github.sskorol.model.DataSupplierMetaData;
import org.testng.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

import static io.github.sskorol.utils.ReflectionUtils.getDataSupplierAnnotation;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

/**
 * Core listener which transforms custom DataSupplier format to common TestNG DataProvider.
 */
public class DataProviderTransformer implements IAnnotationTransformer {

    @DataProvider
    public Iterator<Object[]> supplySeqData(final ITestContext context, final Method testMethod) {
        return getMetaData(context, testMethod).getTestData().iterator();
    }

    @DataProvider(parallel = true)
    public Iterator<Object[]> supplyParallelData(final ITestContext context, final Method testMethod) {
        return getMetaData(context, testMethod).getTestData().iterator();
    }

    @Override
    public void transform(final ITestAnnotation annotation, final Class testClass,
                          final Constructor testConstructor, final Method testMethod) {
        final DataSupplier dataSupplierAnnotation = getDataSupplierAnnotation(
                ofNullable(annotation.getDataProviderClass())
                        .map(dpc -> (Class) dpc)
                        .orElseGet(testMethod::getDeclaringClass),
                annotation.getDataProvider());

        if (!annotation.getDataProvider().isEmpty() && nonNull(dataSupplierAnnotation)) {
            annotation.setDataProviderClass(getClass());
            annotation.setDataProvider(dataSupplierAnnotation.runInParallel() ? "supplyParallelData" : "supplySeqData");
        }
    }

    private DataSupplierMetaData getMetaData(final ITestContext context, final Method testMethod) {
        return new DataSupplierMetaData(context, testMethod);
    }
}
