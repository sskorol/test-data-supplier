package io.github.sskorol.converters;

import java.time.LocalDate;

import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Optional.ofNullable;

public class LocalDateConverter extends DefaultConverter<LocalDate> {
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd";

    @Override
    public LocalDate convert(final String value) {
        return convert(value, DEFAULT_FORMAT);
    }

    @Override
    public LocalDate convert(final String value, final String format) {
        return parse(value, ofPattern(ofNullable(format).filter(f -> !f.isEmpty()).orElse(DEFAULT_FORMAT)));
    }
}
