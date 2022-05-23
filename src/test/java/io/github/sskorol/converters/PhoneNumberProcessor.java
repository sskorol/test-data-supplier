package io.github.sskorol.converters;

public class PhoneNumberProcessor extends DefaultConverter<String> {

    private static final String INDIA_COUNTRY_CODE = "+91";

    @Override
    public String convert(final String value) {
        return value.startsWith(INDIA_COUNTRY_CODE) ? value : (INDIA_COUNTRY_CODE + value);
    }
}
