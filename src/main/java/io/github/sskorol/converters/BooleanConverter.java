package io.github.sskorol.converters;

import java.lang.reflect.Type;

import static java.lang.Boolean.parseBoolean;

public class BooleanConverter extends DefaultConverter<Boolean> {
    @Override
    public Boolean convert(final String value) {
        return parseBoolean(value);
    }

    @Override
    public Type getType() {
        return Boolean.TYPE;
    }
}
