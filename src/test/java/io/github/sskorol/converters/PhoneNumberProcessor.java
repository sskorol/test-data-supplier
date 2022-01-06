package io.github.sskorol.converters;

import com.creditdatamw.zerocell.converter.Converter;

public class PhoneNumberProcessor implements Converter<String> {
    @Override
    public String convert(String value, String s1, int i) {
        return value.startsWith("+91") ? value : "+91"+value;
    }
}
