package io.github.sskorol.model;

import io.github.sskorol.core.DataSupplier;
import io.vavr.Tuple;
import lombok.Getter;
import one.util.streamex.*;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.sskorol.utils.ReflectionUtils.getMethod;
import static io.github.sskorol.utils.ReflectionUtils.invoke;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.Predicates.isNull;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

/**
 * Base container for DataSupplier meta data.
 */
public class DataSupplierMetaData {

    @Getter
    private final Method testMethod;
    @Getter
    private final Method dataSupplierMethod;
    @Getter
    private final List<Object[]> testData;
    private final boolean transpose;
    private final boolean flatMap;
    private final ITestContext context;

    public DataSupplierMetaData(final ITestContext context, final Method testMethod) {
        this.context = context;
        this.testMethod = testMethod;

        final Test testAnnotation = testMethod.getDeclaredAnnotation(Test.class);
        final Class<?> dataSupplierClass = testAnnotation.dataProviderClass() != Object.class
                ? testAnnotation.dataProviderClass()
                : testMethod.getDeclaringClass();
        final String dataSupplierName = testAnnotation.dataProvider();

        this.dataSupplierMethod = getMethod(dataSupplierClass, dataSupplierName);
        final Optional<DataSupplier> dataSupplier =
                ofNullable(dataSupplierMethod.getDeclaredAnnotation(DataSupplier.class));
        this.transpose = dataSupplier.map(DataSupplier::transpose).orElse(false);
        this.flatMap = dataSupplier.map(DataSupplier::flatMap).orElse(false);
        this.testData = transform();
    }

    private List<Object[]> transform() {
        final StreamEx<?> wrappedDataSupplierReturnValue = wrap(obtainReturnValue());
        return transpose
                ? singletonList(flatMap ? wrappedDataSupplierReturnValue.flatMap(this::wrap).toArray()
                : wrappedDataSupplierReturnValue.toArray())
                : wrappedDataSupplierReturnValue.map(ob -> flatMap ? wrap(ob).toArray()
                : new Object[]{ob}).toList();
    }

    private StreamEx<?> wrap(final Object value) {
        return Match(value).of(
                Case($(isNull()), () -> {
                    throw new IllegalArgumentException(format(
                            "Nothing to return from data supplier. The following test will be skipped: %s.%s.",
                            testMethod.getDeclaringClass().getSimpleName(),
                            testMethod.getName()));
                }),
                Case($(instanceOf(Collection.class)), d -> StreamEx.of((Collection<?>) d)),
                Case($(instanceOf(Map.class)), d -> EntryStream.of((Map<?, ?>) d).mapKeyValue(SimpleEntry::new)),
                Case($(instanceOf(Map.Entry.class)), d -> StreamEx.of(d.getKey(), d.getValue())),
                Case($(instanceOf(Object[].class)), d -> StreamEx.of((Object[]) d)),
                Case($(instanceOf(double[].class)), d -> DoubleStreamEx.of((double[]) d).boxed()),
                Case($(instanceOf(int[].class)), d -> IntStreamEx.of((int[]) d).boxed()),
                Case($(instanceOf(long[].class)), d -> LongStreamEx.of((long[]) d).boxed()),
                Case($(instanceOf(Stream.class)), d -> StreamEx.of((Stream<?>) d)),
                Case($(instanceOf(Tuple.class)), d -> StreamEx.of(((Tuple) d).toSeq().toJavaArray())),
                Case($(), d -> StreamEx.of(d)));
    }

    private Object obtainReturnValue() {
        return invoke(dataSupplierMethod, () -> stream(dataSupplierMethod.getParameterTypes())
                .map(t -> Match((Class) t).of(
                        Case($(ITestContext.class), () -> context),
                        Case($(Method.class), () -> testMethod),
                        Case($(), () -> null)))
                .toArray());
    }
}
