package io.github.sskorol.testcases;

import io.github.sskorol.core.DataProviderTransformer;
import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import io.github.sskorol.stubs.IDataProvidableImpl;
import io.github.sskorol.utils.ReflectionUtils;
import io.github.sskorol.utils.ServiceLoaderUtils;
import io.vavr.Tuple;
import one.util.streamex.StreamEx;
import org.testng.ITestNGListener;
import org.testng.annotations.Test;

import java.util.List;

import static io.github.sskorol.utils.ReflectionUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.joor.Reflect.onClass;

public class ReflectionUtilsTests {

    @DataSupplier(flatMap = true)
    public List<Tuple> getDataSupplierClassData() {
        return List.of(
            Tuple.of(new IDataProvidableImpl<>(ExternalDataSuppliers.class),
                     ExternalDataSuppliers.class,
                     ArraysDataSupplierTests.class,
                     ArraysDataSupplierTests.class
            ),
            Tuple.of(new IDataProvidableImpl<>(null),
                     ExternalDataSuppliers.class,
                     ChildTest.class,
                     ParentTest.class
            )
        );
    }

    @Test
    public void shouldThrowAnExceptionOnReflectionUtilsConstructorAccess() {
        assertThatThrownBy(() -> onClass(ReflectionUtils.class).create())
            .hasStackTraceContaining("java.lang.UnsupportedOperationException: This is a utility class and cannot be " +
                                     "instantiated");
    }

    @Test
    public void shouldThrowAnExceptionOnServiceLoaderUtilsConstructorAccess() {
        assertThatThrownBy(() -> onClass(ServiceLoaderUtils.class).create())
            .hasStackTraceContaining("java.lang.UnsupportedOperationException: This is a utility class and cannot be " +
                                     "instantiated");
    }

    @Test(expectedExceptions = NoSuchMethodException.class)
    public void shouldThrowAnExceptionOnNonExistingMethodAccess() {
        getDataSupplierMethod(ExternalDataSuppliers.class, "missingMethodName");
    }

    @Test
    public void shouldReturnEmptyCollectionInCaseOfException() {
        assertThat(ServiceLoaderUtils.load(null, null)).isEmpty();
    }

    @Test
    public void shouldLoadDataProviderTransformerService() {
        var serviceImplementations = ServiceLoaderUtils.load(ITestNGListener.class, this.getClass().getClassLoader());
        assertThat(serviceImplementations).hasSize(1);
        assertThat(serviceImplementations.get(0)).isInstanceOf(DataProviderTransformer.class);
    }

    @Test
    public void shouldNotCastToArray() {
        assertThat(castToArray(String[].class)).isEqualTo(String[].class);
    }

    @Test
    public void shouldCastToArray() {
        assertThat(castToArray(String.class)).isEqualTo(String[].class);
    }

    @Test
    public void shouldCastToObject() {
        assertThat(castToObject(String[].class)).isEqualTo(String.class);
    }

    @Test
    public void shouldNotCastToObject() {
        assertThat(castToObject(String.class)).isEqualTo(String.class);
    }

    @Test(dataProvider = "getDataSupplierClassData")
    public void shouldGetDataSupplierClass(
        final IDataProvidableImpl<?> iDataProvidable,
        final Class<?> expectedClass,
        final Class<?> testClass,
        final Class<?> testMethodClass
    ) {
        var testMethod = StreamEx.of(testMethodClass.getMethods())
            .findFirst()
            .orElseThrow(() -> new AssertionError("No methods found in " + testClass.getName()));
        var actualClass = getDataSupplierClass(iDataProvidable, testClass, testMethod);

        assertThat(actualClass).isEqualTo(expectedClass);
    }
}
