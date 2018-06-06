package io.github.sskorol.model;

import io.github.sskorol.core.DataSupplier;
import io.vavr.Tuple;
import lombok.Getter;
import lombok.val;
import one.util.streamex.*;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;

import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.github.sskorol.utils.ReflectionUtils.findDataSupplier;
import static io.github.sskorol.utils.ReflectionUtils.invokeDataSupplier;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.Predicates.isNull;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

/**
 * Base container for DataSupplier meta data.
 */
@SuppressWarnings("FinalLocalVariable")
public class DataSupplierMetaData {

    @Getter
    private final ITestNGMethod testMethod;
    @Getter
    private final Method dataSupplierMethod;
    @Getter
    private final List<Object[]> testData;
    private final boolean transpose;
    private final boolean flatMap;
    private final int[] indices;
    private final ITestContext context;

    public DataSupplierMetaData(final ITestContext context, final ITestNGMethod testMethod) {
        this.context = context;
        this.testMethod = testMethod;
        this.dataSupplierMethod = findDataSupplier(testMethod);

        val dataSupplier = ofNullable(dataSupplierMethod.getDeclaredAnnotation(DataSupplier.class));
        this.transpose = dataSupplier.map(DataSupplier::transpose).orElse(false);
        this.flatMap = dataSupplier.map(DataSupplier::flatMap).orElse(false);
        this.indices = dataSupplier.map(DataSupplier::indices).orElse(new int[0]);
        this.testData = transform();
    }

    private List<Object[]> transform() {
        val data = wrap(obtainReturnValue()).toList();
        val indicesList = indicesList(data.size());
        val wrappedReturnValue = EntryStream.of(data).filterKeys(indicesList::contains).values();

        if (transpose) {
            return singletonList(flatMap
                    ? wrappedReturnValue.flatMap(this::wrap).toArray()
                    : wrappedReturnValue.toArray());
        }

        return wrappedReturnValue.map(ob -> flatMap ? wrap(ob).toArray() : new Object[]{ob}).toList();
    }

    private Object obtainReturnValue() {
        return invokeDataSupplier(dataSupplierMethod, () -> stream(dataSupplierMethod.getParameterTypes())
                .map(t -> Match((Class) t).of(
                        Case($(ITestContext.class), () -> context),
                        Case($(Method.class), () -> testMethod.getConstructorOrMethod().getMethod()),
                        Case($(ITestNGMethod.class), () -> testMethod),
                        Case($(), () -> null)))
                .toArray());
    }

    private StreamEx<?> wrap(final Object data) {
        return Match(data).of(
                Case($(isNull()), () -> {
                    throw new IllegalArgumentException("Nothing to return from data supplier. Test will be skipped.");
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

    private List<Integer> indicesList(final int collectionSize) {
        return ofNullable(indices)
                .filter(indicesArray -> indicesArray.length > 0)
                .map(IntStreamEx::of)
                .orElseGet(() -> IntStreamEx.range(0, collectionSize))
                .boxed()
                .toList();
    }
}
