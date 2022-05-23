package io.github.sskorol.converters;

import java.lang.reflect.Type;

public interface IConverter<T> {
    T convert(String value);

    Type getType();

    default T convert(final String value, final String format) {
        return convert(value);
    }
}
