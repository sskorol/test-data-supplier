package io.github.sskorol.model;

import io.github.sskorol.core.DataSupplier;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.Getter;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;

import java.lang.reflect.Method;
import java.util.function.Function;

import static io.github.sskorol.utils.ReflectionUtils.findDataSupplier;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

/**
 * Internal entity required for storing TestNG meta data retrieved from listeners.
 */
public class TestNGMetaData {

    @Getter
    private final ITestNGMethod testMethod;
    @Getter
    private final Method dataSupplierMethod;
    private final ITestContext context;
    private final DataSupplier dataSupplier;

    public TestNGMetaData(final ITestContext context, final ITestNGMethod testMethod) {
        this.context = context;
        this.testMethod = testMethod;
        this.dataSupplierMethod = findDataSupplier(testMethod);
        this.dataSupplier = dataSupplierMethod.getDeclaredAnnotation(DataSupplier.class);
    }

    public <T> T getDataSupplierArg(final Function<DataSupplier, T> mapper, final T other) {
        return ofNullable(dataSupplier).map(mapper).orElse(other);
    }

    public Tuple2<Method, Object[]> getDataSupplierMetaData() {
        return Tuple.of(dataSupplierMethod, stream(dataSupplierMethod.getParameterTypes())
                .map(t -> Match((Class) t).of(
                        Case($(ITestContext.class), () -> context),
                        Case($(Method.class), () -> testMethod.getConstructorOrMethod().getMethod()),
                        Case($(ITestNGMethod.class), () -> testMethod),
                        Case($(), () -> null)))
                .toArray());
    }
}
