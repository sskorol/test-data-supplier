package io.github.sskorol.dataprovider;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

import static io.github.sskorol.dataprovider.ReflectionUtils.isAnnotationPresent;
import static java.util.Optional.ofNullable;

/**
 * Core listener which transforms custom DataSupplier format to common TestNG DataProvider.
 */
public class DataProviderTransformer implements IAnnotationTransformer {

    @Override
    public void transform(final ITestAnnotation annotation, final Class testClass,
                          final Constructor testConstructor, final Method testMethod) {
        final boolean isDataSupplierAnnotationPresent = isAnnotationPresent(
                ofNullable(annotation.getDataProviderClass())
                        .map(dpc -> (Class) dpc)
                        .orElseGet(testMethod::getDeclaringClass),
                annotation.getDataProvider(),
                DataSupplier.class);

        if (!annotation.getDataProvider().isEmpty() && isDataSupplierAnnotationPresent) {
            annotation.setDataProviderClass(getClass());
            annotation.setDataProvider("supplyData");
        }
    }

    @DataProvider
    public Iterator<Object[]> supplyData(final ITestContext context, final Method testMethod) {
        return new DataSupplierMetaData(context, testMethod).transform();
    }
}
