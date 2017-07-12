package io.github.sskorol.dataprovider;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.SneakyThrows;
import one.util.streamex.StreamEx;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Supplier;

import static org.joor.Reflect.on;

/**
 * Simple utility class for internal DataSupplier management.
 */
public final class ReflectionUtils {

    private ReflectionUtils() {
        throw new UnsupportedOperationException("Illegal access to private constructor");
    }

    @SneakyThrows(NoSuchMethodException.class)
    public static Method getMethod(final Class<?> targetClass, final String targetMethodName) {
        final Tuple2<String, Class<?>[]> metaData = StreamEx.of(targetClass.getMethods())
                                            .map(m -> Tuple.of(m, m.getDeclaredAnnotation(DataSupplier.class)))
                                            .filter(t -> Objects.nonNull(t._2)
                                                    && (t._2.name().equals(targetMethodName)
                                                    || t._1.getName().equals(targetMethodName)))
                                            .map(t -> Tuple.of(t._1.getName(), t._1.getParameterTypes()))
                                            .findFirst()
                                            .orElseGet(() -> Tuple.of(targetMethodName, new Class<?>[0]));

        return targetClass.getMethod(metaData._1, metaData._2);
    }

    public static DataSupplier getDataSupplierAnnotation(final Class<?> targetClass, final String targetMethodName) {
        return Try.of(() -> getMethod(targetClass, targetMethodName))
                  .map(m -> m.getDeclaredAnnotation(DataSupplier.class))
                  .filter(Objects::nonNull)
                  .getOrElse((DataSupplier) null);
    }

    public static Object invoke(final Method method, final Supplier<Object[]> argsSupplier) {
        return on(method.getDeclaringClass())
                .create()
                .call(method.getName(), argsSupplier.get())
                .get();
    }
}
