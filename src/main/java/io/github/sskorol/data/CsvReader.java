package io.github.sskorol.data;

import io.github.sskorol.utils.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import one.util.streamex.StreamEx;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.lang.String.format;
import static org.apache.commons.csv.CSVParser.parse;
import static org.joor.Reflect.onClass;

@AllArgsConstructor
public class CsvReader<T> implements DataReader<T> {

    @Getter
    private final Class<T> entityClass;
    @Getter
    private final String path;

    public CsvReader(final Class<T> entityClass) {
        this(entityClass, "");
    }

    @Override
    public StreamEx<T> read() {
        try (val csvParser = parse(getUrl(), StandardCharsets.UTF_8,
                CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).setIgnoreHeaderCase(true).setTrim(true).build())) {
            val entityFields = StreamEx.of(entityClass.getDeclaredFields()).map(ReflectionUtils::getFieldName).toList();
            return StreamEx.of(csvParser.getRecords())
                    .map(csvRecord -> StreamEx.of(entityFields).map(csvRecord::get).toArray())
                    .map(args -> onClass(entityClass).create(args).get());
        } catch (IOException ex) {
            throw new IllegalArgumentException(
                    format("Unable to read JSON data to %s. Check provided path.", entityClass), ex);
        }
    }
}
