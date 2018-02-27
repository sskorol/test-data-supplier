package io.github.sskorol.utils;

import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import one.util.streamex.StreamEx;

import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

/**
 * SPI management internals for dynamic listeners' implementation loading.
 */
@UtilityClass
public class ServiceLoaderUtils {

    public static <T> List<T> load(final Class<T> type, final ClassLoader classLoader) {
        return Try.of(() -> StreamEx.of(ServiceLoader.load(type, classLoader).iterator()).toList())
                  .getOrElseGet(ex -> Collections.emptyList());
    }
}
