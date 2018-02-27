package io.github.sskorol.utils;

import io.github.sskorol.core.DataSupplier;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import one.util.streamex.StreamEx;
import org.reflections.Reflections;
import org.testng.annotations.Test;
import org.testng.internal.annotations.IDataProvidable;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static org.joor.Reflect.on;

/**
 * Simple utility class for internal DataSupplier management.
 */
@UtilityClass
public class ReflectionUtils {

    @SuppressWarnings("unchecked")
    public static Class<?> getDataSupplierClass(final IDataProvidable annotation, final Class testClass,
                                                final Method testMethod) {
        return ofNullable(annotation.getDataProviderClass())
                .map(dataProviderClass -> (Class) dataProviderClass)
                .orElseGet(() -> findParentDataSupplierClass(testMethod, testClass));
    }

    public static Class<?> findParentDataSupplierClass(final Method testMethod, final Class testClass) {
        return ofNullable(testMethod)
                .map(m -> Tuple.of(m, new Reflections(m.getDeclaringClass().getPackage().getName())))
                .map(t -> StreamEx.of(t._2.getSubTypesOf(t._1.getDeclaringClass()))
                                  .findFirst(c -> c.isAnnotationPresent(Test.class))
                                  .map(c -> c.getDeclaredAnnotation(Test.class))
                                  .map(a -> (Class) a.dataProviderClass())
                                  .orElse(t._1.getDeclaringClass()))
                .orElse(testClass);
    }

    @SneakyThrows(NoSuchMethodException.class)
    @SuppressWarnings("FinalLocalVariable")
    public static Method getDataSupplierMethod(final Class<?> targetClass, final String targetMethodName) {
        val methodMetaData = StreamEx.of(targetClass.getMethods())
                                     .map(method -> Tuple.of(method, method.getDeclaredAnnotation(DataSupplier.class)))
                                     .filter(hasDataSupplierMethod(targetMethodName))
                                     .map(metaData -> Tuple.of(metaData._1.getName(), metaData._1.getParameterTypes()))
                                     .findFirst()
                                     .orElseGet(() -> Tuple.of(targetMethodName, new Class<?>[0]));

        return targetClass.getMethod(methodMetaData._1, methodMetaData._2);
    }

    public static DataSupplier getDataSupplierAnnotation(final Class<?> targetClass, final String targetMethodName) {
        return Try.of(() -> getDataSupplierMethod(targetClass, targetMethodName))
                  .map(method -> method.getDeclaredAnnotation(DataSupplier.class))
                  .filter(Objects::nonNull)
                  .getOrElse((DataSupplier) null);
    }

    public static Object invokeDataSupplier(final Method method, final Supplier<Object[]> argsSupplier) {
        return on(method.getDeclaringClass())
                .create()
                .call(method.getName(), argsSupplier.get())
                .get();
    }

    private static Predicate<Tuple2<Method, DataSupplier>> hasDataSupplierMethod(final String targetMethodName) {
        return metaData -> Objects.nonNull(metaData._2)
                && (metaData._2.name().equals(targetMethodName) || metaData._1.getName().equals(targetMethodName));
    }
}
