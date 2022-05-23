package io.github.sskorol.converters;

import java.lang.reflect.Type;

import static java.lang.Integer.parseInt;

public class IntegerConverter extends DefaultConverter<Integer> {
    @Override
    public Integer convert(final String value) {
        return parseInt(value);
    }

    @Override
    public Type getType() {
        return Integer.TYPE;
    }
}
