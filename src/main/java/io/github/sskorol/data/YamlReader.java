package io.github.sskorol.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import one.util.streamex.StreamEx;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.lang.String.format;

@Getter
@RequiredArgsConstructor
public class YamlReader<T> implements DataReader<T> {

    private final Class<T> entityClass;
    private final String path;

    public YamlReader(final Class<T> entityClass) {
        this(entityClass, "");
    }

    @SuppressWarnings("unchecked")
    public StreamEx<T> read() {
        try {
            val yamlFactory = new YAMLFactory();
            return StreamEx.of(new ObjectMapper(yamlFactory)
                    .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValues(yamlFactory.createParser(getUrl()), entityClass)
                    .readAll());
        } catch (Exception ex) {
            throw new IllegalArgumentException(format("Unable to read YAML data to %s.", entityClass), ex);
        }
    }
}
