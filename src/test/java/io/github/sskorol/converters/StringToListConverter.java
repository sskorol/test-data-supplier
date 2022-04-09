package io.github.sskorol.converters;

import com.creditdatamw.zerocell.converter.Converter;

import java.util.Arrays;
import java.util.List;

public class StringToListConverter implements Converter<List<String>> {

    @Override
    public List<String> convert(final String value, final String columnName, final int row) {
        return Arrays.asList(value.split(","));
    }
}
