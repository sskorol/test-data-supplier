package io.github.sskorol.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import one.util.streamex.StreamEx;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static com.google.gson.JsonParser.parseReader;
import static io.github.sskorol.utils.ReflectionUtils.castToArray;
import static io.github.sskorol.utils.ReflectionUtils.castToObject;
import static io.vavr.API.*;
import static java.lang.String.format;

@AllArgsConstructor
public class JsonReader<T> implements DataReader<T> {

    @Getter
    private final Class<T> entityClass;
    @Getter
    private final String path;

    public JsonReader(final Class<T> entityClass) {
        this(entityClass, "");
    }

    @SuppressWarnings("unchecked")
    public StreamEx<T> read() {
        var gson = new Gson();
        try (var streamReader = new InputStreamReader(getUrl().openStream(), StandardCharsets.UTF_8);
             var jsonReader = new com.google.gson.stream.JsonReader(streamReader)) {
            return StreamEx.of((T[]) Match(parseReader(jsonReader)).of(
                Case($(JsonElement::isJsonArray), j -> gson.fromJson(j, castToArray(entityClass))),
                Case($(), j -> (T[]) new Object[] {gson.fromJson(j, castToObject(entityClass))})
            ));
        } catch (IOException ex) {
            throw new IllegalArgumentException(
                format("Unable to read JSON data to %s. Check provided path.", entityClass), ex);
        }
    }
}
