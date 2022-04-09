package io.github.sskorol.data;

import lombok.AllArgsConstructor;
import one.util.streamex.StreamEx;

import java.util.Objects;

import static org.joor.Reflect.onClass;

/**
 * Generic structure for reading test data in JSON, CSV and YAML formats. Each format has it's own implementation,
 * which should be passed to {@link #use(Class)} method.
 * <pre>{@code
 * use(CsvReader.class).withTarget(User.class).withSource("users.csv").read();
 * use(YamlReader.class).withTarget(TravisConfiguration.class).read();
 * use(JsonReader.class).withTarget(Animal.class).withSource("http://animals.json").read();
 * }</pre>
 * Please note that {@link io.github.sskorol.data.TestDataReader.DataBuilder#withSource(String)} is optional. But if
 * it's skipped, you have to use {@link Source} annotation to define source path.
 *
 * @param <T> {@link DataReader} implementation class.
 */
@AllArgsConstructor
public class TestDataReader<T extends DataReader<?>> {

    private final Class<T> dataReaderClass;

    public static <T extends DataReader<?>> TestDataReader<T> use(final Class<T> dataReaderClass) {
        return new TestDataReader<>(dataReaderClass);
    }

    public <E> DataBuilder<T, E> withTarget(final Class<E> entityClass) {
        return new DataBuilder<>(dataReaderClass, entityClass);
    }

    public static class DataBuilder<T, E> {

        private final Class<T> dataReaderClass;
        private final Class<E> entityClass;
        private String path;

        public DataBuilder(final Class<T> dataReaderClass, final Class<E> entityClass) {
            this.dataReaderClass = dataReaderClass;
            this.entityClass = entityClass;
        }

        public DataBuilder<T, E> withSource(final String path) {
            this.path = path;
            return this;
        }

        public StreamEx<E> read() {
            var args = StreamEx.of(entityClass, path).filter(Objects::nonNull).toArray();
            return onClass(dataReaderClass).create(args).call("read").get();
        }
    }
}
