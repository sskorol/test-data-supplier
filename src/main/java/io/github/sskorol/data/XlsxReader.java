package io.github.sskorol.data;

import io.github.sskorol.converters.*;
import lombok.Getter;
import one.util.streamex.StreamEx;
import org.apache.poi.ss.usermodel.*;

import java.util.*;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.joor.Reflect.onClass;

public class XlsxReader<T> implements DataReader<T> {

    @Getter
    private final Class<T> entityClass;
    @Getter
    private final String path;
    private final List<IConverter<T>> defaultIConverters;

    public XlsxReader(final Class<T> entityClass, final String path) {
        this.entityClass = entityClass;
        this.path = path;
        this.defaultIConverters = defaultConverters();
    }

    public XlsxReader(final Class<T> entityClass) {
        this(entityClass, "");
    }

    @SuppressWarnings("unchecked")
    public static <T> List<IConverter<T>> defaultConverters() {
        return StreamEx.of(
                BooleanConverter.class,
                StringConverter.class,
                IntegerConverter.class,
                DoubleConverter.class,
                LocalDateConverter.class,
                LocalDateTimeConverter.class
            )
            .map(cls -> (IConverter<T>) onClass(cls).create().get())
            .toList();
    }

    public StreamEx<T> read() {
        try (var workbook = WorkbookFactory.create(getUrl().openStream())) {
            var formatter = new DataFormatter();
            var startIndex = 0;
            var skip = 1;

            var sheet = ofNullable(entityClass.getDeclaredAnnotation(Sheet.class))
                .map(annotation -> workbook.getSheet(annotation.name()))
                .orElse(workbook.getSheetAt(startIndex));

            var headers = StreamEx.of(sheet.getRow(startIndex).cellIterator())
                .toMap(cell -> formatter.formatCellValue(cell).trim(), Cell::getColumnIndex);

            var cellMappers = StreamEx.of(entityClass.getDeclaredFields())
                .map(field -> new XlsxCellMapper<>(field, headers, defaultIConverters))
                .toList();

            return StreamEx.of(sheet.iterator())
                .skip(skip)
                .map(row -> StreamEx.of(cellMappers).map(cellMapper -> cellMapper.parse(row)).toList())
                .map(this::initEntity);
        } catch (Exception ex) {
            throw new IllegalArgumentException(format("Unable to read XLSX data to %s.", entityClass), ex);
        }
    }

    @SuppressWarnings("unchecked")
    public T initEntity(final List<XlsxCellMapper<T>> mappers) {
        var hasDefaultConstructor = StreamEx.of(entityClass.getDeclaredConstructors())
            .anyMatch(constructor -> constructor.getParameterCount() == 0);
        if (!hasDefaultConstructor) {
            throw new IllegalStateException(format("%s must have default constructor.", entityClass.getSimpleName()));
        }

        var entity = (T) onClass(entityClass).create().get();
        StreamEx.of(mappers).forEach(mapper -> mapper.assignValue(entity));

        return entity;
    }
}
