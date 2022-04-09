package io.github.sskorol.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import one.util.streamex.StreamEx;
import com.creditdatamw.zerocell.Reader;

import java.io.File;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@AllArgsConstructor
public class XlsxReader<T> implements DataReader<T> {

    @Getter
    private final Class<T> entityClass;
    @Getter
    private final String path;

    public XlsxReader(final Class<T> entityClass) {
        this(entityClass, "");
    }

    public StreamEx<T> read() {
        try {
            val builder = Reader.of(entityClass).from(new File(getUrl().getFile()));
            return StreamEx.of(
                ofNullable(entityClass.getDeclaredAnnotation(Sheet.class))
                    .map(Sheet::name)
                    .map(builder::sheet)
                    .orElse(builder)
                    .skipEmptyRows(true)
                    .list()
            );
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                format("Unable to read XLSX data to %s. Check provided path.", entityClass), ex);
        }
    }
}
