package io.github.sskorol.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import one.util.streamex.StreamEx;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.lang.String.format;

@AllArgsConstructor
public class YamlReader<T> implements DataReader<T> {

    @Getter
    private final Class<T> entityClass;
    @Getter
    private final String path;

    public YamlReader(final Class<T> entityClass) {
        this(entityClass, "");
    }

    public StreamEx<T> read() {
        try {
            var yamlFactory = new YAMLFactory();
            return StreamEx.of(new ObjectMapper(yamlFactory)
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValues(yamlFactory.createParser(getUrl()), entityClass)
                .readAll());
        } catch (Exception ex) {
            throw new IllegalArgumentException(format("Unable to read YAML data to %s.", entityClass), ex);
        }
    }
}
