package io.github.sskorol.core;

import io.github.sskorol.model.DataSupplierMetaData;
import lombok.val;
import org.testng.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

import static io.github.sskorol.utils.ReflectionUtils.getDataSupplierAnnotation;
import static io.github.sskorol.utils.ReflectionUtils.getDataSupplierClass;
import static java.util.Objects.nonNull;

/**
 * Core listener which transforms custom DataSupplier format to common TestNG DataProvider.
 */
public class DataProviderTransformer implements IAnnotationTransformer {

    @DataProvider
    public Iterator<Object[]> supplySequentialData(final ITestContext context, final Method testMethod) {
        return getDataSupplierMetaData(context, testMethod).getTestData().iterator();
    }

    @DataProvider(parallel = true)
    public Iterator<Object[]> supplyParallelData(final ITestContext context, final Method testMethod) {
        return getDataSupplierMetaData(context, testMethod).getTestData().iterator();
    }

    @Override
    @SuppressWarnings({"unchecked", "FinalLocalVariable"})
    public void transform(final ITestAnnotation annotation, final Class testClass,
                          final Constructor testConstructor, final Method testMethod) {
        val dataSupplierClass = getDataSupplierClass(annotation, testClass, testMethod);
        val dataSupplierAnnotation = getDataSupplierAnnotation(dataSupplierClass, annotation.getDataProvider());

        if (!annotation.getDataProvider().isEmpty() && nonNull(dataSupplierAnnotation)) {
            annotation.setDataProviderClass(getClass());
            annotation.setDataProvider(dataSupplierAnnotation.runInParallel()
                    ? "supplyParallelData"
                    : "supplySequentialData");
        }
    }

    private DataSupplierMetaData getDataSupplierMetaData(final ITestContext context, final Method testMethod) {
        return new DataSupplierMetaData(context, testMethod);
    }
}
