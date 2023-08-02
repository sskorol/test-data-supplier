package io.github.sskorol.data;

import io.github.sskorol.converters.IConverter;
import one.util.streamex.StreamEx;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;
import static org.joor.Reflect.on;
import static org.joor.Reflect.onClass;

public class XlsxCellMapper<T> {
    private final int index;
    private final Field field;
    private final IConverter<T> converter;
    private final DataFormatter formatter;
    private final List<IConverter<T>> defaultIConverters;

    private T convertedValue;

    public XlsxCellMapper(
        final Field field,
        final Map<String, Integer> headers,
        final List<IConverter<T>> defaultIConverters
    ) {
        this.field = field;
        this.defaultIConverters = defaultIConverters;
        this.index = getColumn()
            .map(Column::name)
            .filter(headers::containsKey)
            .map(headers::get)
            .orElseThrow(() -> new IllegalArgumentException(format("Column %s not found", field.getName())));
        this.converter = findMatchingConverter();
        this.formatter = new DataFormatter();
    }

    public XlsxCellMapper<T> parse(final Row row) {
        var cellValue = ofNullable(row.getCell(index, RETURN_BLANK_AS_NULL))
            .map(cell -> formatter.formatCellValue(cell).trim())
            .orElse("");
        this.convertedValue = converter.convert(cellValue, getColumn().map(Column::format).orElse(""));
        return this;
    }

    public <R> void assignValue(final R instance) {
        ofNullable(convertedValue)
            .ifPresent(value -> on(instance).set(getFieldName(), value));
    }

    private String getFieldName() {
        return field.getName();
    }

    private Type getFieldType() {
        return field.getType();
    }

    private Optional<Column> getColumn() {
        return ofNullable(field.getDeclaredAnnotation(Column.class));
    }

    @SuppressWarnings("unchecked")
    private IConverter<T> findMatchingConverter() {
        return getColumn()
            .map(Column::converter)
            .map(converterClass -> (IConverter<T>) onClass(converterClass).create().get())
            .filter(converterInstance -> converterInstance.getType().equals(getFieldType()))
            .orElseGet(() -> StreamEx
                .of(defaultIConverters)
                .findFirst(converterInstance -> converterInstance.getType().equals(getFieldType()))
                .orElseThrow(() -> new IllegalStateException(format(
                    "There's no matching converter found for %s field of type %s",
                    getFieldName(), getFieldType()))
                )
            );
    }
}
