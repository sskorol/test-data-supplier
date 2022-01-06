package io.github.sskorol.converters;

import com.creditdatamw.zerocell.converter.Converter;
import java.util.Arrays;
import java.util.List;

public class StringToListConverter implements Converter<List<String>> {

    @Override
    public List<String> convert(String value, String columnName, int row) {
        return Arrays.asList(value.split(","));
    }
}
