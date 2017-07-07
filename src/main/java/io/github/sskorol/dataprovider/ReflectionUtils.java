package io.github.sskorol.dataprovider;

import io.vavr.control.Try;
import lombok.SneakyThrows;
import one.util.streamex.StreamEx;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
        final Class<?>[] argTypes = StreamEx.of(targetClass.getMethods())
                                            .findFirst(m -> m.getName().equals(targetMethodName))
                                            .map(Method::getParameterTypes)
                                            .orElseGet(() -> new Class<?>[0]);

        return targetClass.getMethod(targetMethodName, argTypes);
    }

    public static boolean isAnnotationPresent(final Class<?> targetClass, final String targetMethodName,
                                              final Class<? extends Annotation> annotation) {
        return Try.of(() -> getMethod(targetClass, targetMethodName))
                  .map(m -> m.isAnnotationPresent(annotation))
                  .getOrElse(false);
    }

    public static Object invoke(final Method method, final Supplier<Object[]> argsSupplier) {
        return on(method.getDeclaringClass())
                .create()
                .call(method.getName(), argsSupplier.get())
                .get();
    }
}
