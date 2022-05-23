package io.github.sskorol.converters;

import java.lang.reflect.Type;

public abstract class DefaultConverter<T> implements IConverter<T> {
    private final Type type;

    @SafeVarargs
    protected DefaultConverter(final T... values) {
        this.type = values.getClass().getComponentType();
    }

    @Override
    public Type getType() {
        return type;
    }
}
