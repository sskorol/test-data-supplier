package io.github.sskorol.model;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.utils.ReflectionUtils;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.Getter;
import lombok.val;
import one.util.streamex.*;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.github.sskorol.utils.ReflectionUtils.invokeDataSupplier;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.Predicates.isNull;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
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
        this.dataSupplierMethod = findDataSupplier();

        val dataSupplier = ofNullable(dataSupplierMethod.getDeclaredAnnotation(DataSupplier.class));
        this.transpose = dataSupplier.map(DataSupplier::transpose).orElse(false);
        this.flatMap = dataSupplier.map(DataSupplier::flatMap).orElse(false);
        this.indices = dataSupplier.map(DataSupplier::indices).orElse(new int[0]);
        this.testData = transform();
    }

    private Method findDataSupplier() {
        val annotationMetaData = testMethod.isTest() ? getTestAnnotationMetaData() : getFactoryAnnotationMetaData();
        return ReflectionUtils.getDataSupplierMethod(annotationMetaData._1, annotationMetaData._2);
    }

    @SuppressWarnings("unchecked")
    private Tuple2<Class<?>, String> getTestAnnotationMetaData() {
        val declaringClass = testMethod.getConstructorOrMethod().getDeclaringClass();
        val testAnnotation = ofNullable(testMethod.getConstructorOrMethod()
                                                  .getMethod()
                                                  .getDeclaredAnnotation(Test.class))
                .orElseGet(() -> declaringClass.getDeclaredAnnotation(Test.class));
        val dataSupplierClass = ofNullable(testAnnotation)
                .map(Test::dataProviderClass)
                .filter(dp -> dp != Object.class)
                .orElse((Class) declaringClass);

        return Tuple.of(dataSupplierClass, testAnnotation.dataProvider());
    }

    private Tuple2<Class<?>, String> getFactoryAnnotationMetaData() {
        val constructor = testMethod.getConstructorOrMethod().getConstructor();
        val method = testMethod.getConstructorOrMethod().getMethod();

        val factoryAnnotation = nonNull(method)
                ? ofNullable(method.getDeclaredAnnotation(Factory.class))
                : ofNullable(constructor.getDeclaredAnnotation(Factory.class));

        return Tuple.of(factoryAnnotation
                        .map(fa -> (Class) fa.dataProviderClass())
                        .orElse(testMethod.getConstructorOrMethod().getDeclaringClass()),
                factoryAnnotation.map(Factory::dataProvider).orElse(""));
    }

    private List<Object[]> transform() {
        val data = wrap(obtainReturnValue()).toList();
        val indicesList = indicesList(data.size());
        val wrappedReturnValue = EntryStream.of(data).filterKeys(indicesList::contains).values();

        return transpose
                ? singletonList(flatMap ? wrappedReturnValue.flatMap(this::wrap).toArray()
                : wrappedReturnValue.toArray())
                : wrappedReturnValue.map(ob -> flatMap ? wrap(ob).toArray()
                : new Object[]{ob}).toList();
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
                    throw new IllegalArgumentException(format(
                            "Nothing to return from data supplier. The following test will be skipped: %s.%s.",
                            testMethod.getConstructorOrMethod().getDeclaringClass().getSimpleName(),
                            testMethod.getConstructorOrMethod().getName()));
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
