package io.github.sskorol.converters;

import com.creditdatamw.zerocell.converter.Converter;

import java.time.LocalDate;

public class StringToLocalDateConverter implements Converter<LocalDate> {
    @Override
    public LocalDate convert(String string, String s1, int i) {
        //write your logic using datatimeformatter
        return LocalDate.now();
    }
}
