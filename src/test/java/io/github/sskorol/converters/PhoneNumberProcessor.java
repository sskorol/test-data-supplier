package io.github.sskorol.converters;

import com.creditdatamw.zerocell.converter.Converter;

public class PhoneNumberProcessor implements Converter<String> {

    private static final String INDIA_COUNTRY_CODE = "+91";

    @Override
    public String convert(String value, String columnName, int row) {
        return value.startsWith(INDIA_COUNTRY_CODE) ? value : (INDIA_COUNTRY_CODE + value);
    }
}
