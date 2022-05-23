package io.github.sskorol.utils;

import lombok.experimental.UtilityClass;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@UtilityClass
public class NumberUtils {

    public static Number parseNumber(final String value) throws ParseException {
        return NumberFormat.getInstance(Locale.getDefault()).parse(value);
    }
}
