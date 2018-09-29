package io.github.sskorol.data;

import io.github.sskorol.utils.ReflectionUtils;
import one.util.streamex.StreamEx;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.lang.String.format;
import static org.apache.commons.csv.CSVParser.parse;
import static org.joor.Reflect.on;

public class CsvReader<T> implements DataReader<T> {

    private final Class<T> entityClass;
    private final String path;

    public CsvReader(final Class<T> entityClass) {
        this(entityClass, "");
    }

    public CsvReader(final Class<T> entityClass, final String path) {
        this.entityClass = entityClass;
        this.path = path;
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public StreamEx<T> read() {
        try (var csvParser = parse(getUrl(), StandardCharsets.UTF_8,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            var entityFields = StreamEx.of(entityClass.getDeclaredFields()).map(ReflectionUtils::getFieldName).toList();
            return StreamEx.of(csvParser.getRecords())
                .map(record -> StreamEx.of(entityFields).map(record::get).toArray())
                .map(args -> on(entityClass).create(args).get());
        } catch (IOException ex) {
            throw new IllegalArgumentException(
                format("Unable to read JSON data to %s. Check provided path.", entityClass), ex);
        }
    }
}
