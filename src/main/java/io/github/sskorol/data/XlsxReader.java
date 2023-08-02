package io.github.sskorol.data;

import io.github.sskorol.converters.*;
import io.vavr.Tuple;
import lombok.Getter;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.apache.poi.ss.usermodel.*;

import java.util.*;
import java.util.function.Function;

import static java.lang.String.format;
import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.joor.Reflect.onClass;

public class XlsxReader<T> implements DataReader<T> {

    @Getter
    private final Class<T> entityClass;
    @Getter
    private final String path;
    private final List<IConverter<T>> defaultIConverters;
    private final List<String> sheetNames;

    public XlsxReader(final Class<T> entityClass, final String path) {
        this.entityClass = entityClass;
        this.path = path;
        this.defaultIConverters = defaultConverters();
        this.sheetNames = new ArrayList<>();
    }

    public XlsxReader(final Class<T> entityClass) {
        this(entityClass, "");
    }

    @SuppressWarnings("unchecked")
    public static <T> List<IConverter<T>> defaultConverters() {
        return StreamEx
            .of(
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

            var sheets = getSheetNames()
                .map(workbook::getSheet)
                .toMutableList();

            if (sheets.isEmpty()) {
                sheets.add(workbook.getSheetAt(startIndex));
            }

            return StreamEx
                .of(sheets)
                .flatMap(sheet -> StreamEx
                    .of(sheet.iterator())
                    .skip(skip)
                    .filter(row -> !isRowEmpty(row))
                    .map(mappersOf(sheet, startIndex, formatter))
                    .map(this::initEntity)
                );
        } catch (Exception ex) {
            throw new IllegalArgumentException(format("Unable to read XLSX data to %s.", entityClass), ex);
        }
    }

    @SuppressWarnings("unchecked")
    public T initEntity(final List<XlsxCellMapper<T>> mappers) {
        var hasDefaultConstructor = StreamEx
            .of(entityClass.getDeclaredConstructors())
            .anyMatch(constructor -> constructor.getParameterCount() == 0);

        if (!hasDefaultConstructor) {
            throw new IllegalStateException(format("%s must have default constructor.", entityClass.getSimpleName()));
        }

        var entity = (T) onClass(entityClass).create().get();
        StreamEx.of(mappers).forEach(mapper -> mapper.assignValue(entity));

        return entity;
    }

    @Override
    public XlsxReader<T> additionalSources(final List<String> names) {
        this.sheetNames.addAll(names);
        return this;
    }

    private Function<Row, List<XlsxCellMapper<T>>> mappersOf(
        final org.apache.poi.ss.usermodel.Sheet sheet,
        final int startIndex,
        final DataFormatter formatter
    ) {
        var headers = StreamEx
            .of(sheet.getRow(startIndex).cellIterator())
            .map(cell -> Tuple.of(cell, formatter.formatCellValue(cell).trim()))
            .filter(cell -> !cell._2.isEmpty())
            .toMap(cell -> cell._2, pair -> pair._1.getColumnIndex());

        return row -> StreamEx
            .of(entityClass.getDeclaredFields())
            .map(field -> new XlsxCellMapper<>(field, headers, defaultIConverters))
            .map(cellMapper -> cellMapper.parse(row))
            .toList();
    }

    private boolean isRowEmpty(final Row row) {
        return row == null || row.getLastCellNum() <= 0 ||
               IntStreamEx
                   .range(row.getFirstCellNum(), row.getLastCellNum())
                   .mapToObj(row::getCell)
                   .filter(Objects::nonNull)
                   .noneMatch(cell -> cell.getCellType() != BLANK && !cell.toString().isEmpty());
    }

    private StreamEx<String> getSheetNames() {
        return !sheetNames.isEmpty() ?
               StreamEx.of(sheetNames) :
               StreamEx.of(entityClass.getAnnotationsByType(Sheet.class)).map(Sheet::name);
    }
}
