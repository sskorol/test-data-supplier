package io.github.sskorol.converters;

import java.util.List;

import static java.util.Arrays.asList;

public class StringToListIConverter extends DefaultConverter<List<String>> {

    @Override
    public List<String> convert(final String value) {
        return asList(value.split(","));
    }
}
