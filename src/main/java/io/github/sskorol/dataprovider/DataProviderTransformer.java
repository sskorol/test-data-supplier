package io.github.sskorol.dataprovider;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple4;
import io.vavr.control.Try;
import lombok.SneakyThrows;
import one.util.streamex.DoubleStreamEx;
import one.util.streamex.IntStreamEx;
import one.util.streamex.LongStreamEx;
import one.util.streamex.StreamEx;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.Predicates.isNull;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.joor.Reflect.on;

/**
 * Core listener which transforms custom DataSupplier format to common TestNG DataProvider.
 */
public class DataProviderTransformer implements IAnnotationTransformer {

    @Override
    public void transform(final ITestAnnotation annotation, final Class testClass,
                          final Constructor testConstructor, final Method testMethod) {
        final Tuple4<Class<?>, String, ITestContext, Method> metaData = extractTestMetaData(null, testMethod);
        if (!annotation.getDataProvider().isEmpty() && isDataSupplierAnnotationPresent(metaData._1, metaData._2)) {
            annotation.setDataProviderClass(getClass());
            annotation.setDataProvider("supplyData");
        }
    }

    @DataProvider
    public Iterator<Object[]> supplyData(final ITestContext context, final Method testMethod) {
        final Tuple4<Class<?>, String, ITestContext, Method> testMetaData = extractTestMetaData(context, testMethod);
        return extractDataSupplierAnnotation(testMetaData._1, testMetaData._2)
                .map(ds -> extractTestData(testMetaData, ds.extractValues()))
                .map(t -> t._2 ? singletonList(t._1.toArray()).iterator() : t._1.map(ob -> new Object[]{ob}).iterator())
                .orElseGet(Collections::emptyIterator);
    }

    private boolean isDataSupplierAnnotationPresent(final Class<?> dataSupplierClass, final String method) {
        return Try.run(() -> extractDataSupplierAnnotation(dataSupplierClass, method)).isSuccess();
    }

    private Optional<DataSupplier> extractDataSupplierAnnotation(final Class<?> dataSupplierClass,
                                                                 final String method) {
        return ofNullable(extractDataSupplierMetaData(dataSupplierClass, method)
                .getDeclaredAnnotation(DataSupplier.class));
    }

    @SneakyThrows(NoSuchMethodException.class)
    private Method extractDataSupplierMetaData(final Class<?> dataSupplierClass, final String method) {
        final Class[] parameters = StreamEx.of(dataSupplierClass.getMethods())
                                           .findFirst(m -> m.getName().equals(method))
                                           .map(m -> Arrays.stream(m.getParameterTypes()).toArray(Class[]::new))
                                           .orElseGet(() -> new Class[0]);

        return dataSupplierClass.getMethod(method, parameters);
    }

    private Object invokeDataSupplier(final Tuple4<Class<?>, String, ITestContext, Method> metaData) {
        return on(metaData._1).create().call(metaData._2, extractDataSupplierArgs(metaData)).get();
    }

    private Object[] extractDataSupplierArgs(final Tuple4<Class<?>, String, ITestContext, Method> metaData) {
        final Function<Class, Object> argsMatcher = t -> Match(t).of(
                Case($(ITestContext.class), () -> metaData._3),
                Case($(Method.class), () -> metaData._4),
                Case($(), pt -> {
                    throw new IllegalArgumentException(pt + " cannot be injected into DataSupplier signature");
                }));

        return StreamEx.of(extractDataSupplierMetaData(metaData._1, metaData._2).getParameterTypes())
                       .map(argsMatcher)
                       .toArray();
    }

    private Tuple4<Class<?>, String, ITestContext, Method> extractTestMetaData(final ITestContext context,
                                                                               final Method testMethod) {
        return Match(testMethod.getDeclaredAnnotation(Test.class)).of(
                Case($(t -> t.dataProviderClass() != Object.class), t ->
                        Tuple.of(t.dataProviderClass(), t.dataProvider(), context, testMethod)),
                Case($(), t -> Tuple.of(testMethod.getDeclaringClass(), t.dataProvider(), context, testMethod))
        );
    }

    private Tuple2<StreamEx<?>, Boolean> extractTestData(final Tuple4<Class<?>, String, ITestContext, Method> metaData,
                                                         final boolean extractValues) {
        return Tuple.of(Match(invokeDataSupplier(metaData)).of(
                Case($(isNull()), () -> {
                    throw new IllegalArgumentException(format(
                            "Nothing to return from data supplier. The following test will be skipped: %s.%s.",
                            metaData._1.getSimpleName(),
                            metaData._2));
                }),
                Case($(instanceOf(Collection.class)), d -> StreamEx.of((Collection<?>) d)),
                Case($(instanceOf(Object[].class)), d -> StreamEx.of((Object[]) d)),
                Case($(instanceOf(double[].class)), d -> DoubleStreamEx.of((double[]) d).boxed()),
                Case($(instanceOf(int[].class)), d -> IntStreamEx.of((int[]) d).boxed()),
                Case($(instanceOf(long[].class)), d -> LongStreamEx.of((long[]) d).boxed()),
                Case($(instanceOf(Stream.class)), d -> StreamEx.of((Stream<?>) d)),
                Case($(instanceOf(StreamEx.class)), d -> (StreamEx<?>) d),
                Case($(), d -> StreamEx.of(d))), extractValues);
    }
}
