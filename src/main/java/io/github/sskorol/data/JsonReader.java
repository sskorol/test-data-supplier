package io.github.sskorol.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import one.util.streamex.StreamEx;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static io.github.sskorol.utils.ReflectionUtils.castToArray;
import static io.github.sskorol.utils.ReflectionUtils.castToObject;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static java.lang.String.format;

@Getter
@RequiredArgsConstructor
public class JsonReader<T> implements DataReader<T> {

    private final Class<T> entityClass;
    private final String path;

    public JsonReader(final Class<T> entityClass) {
        this(entityClass, "");
    }

    @SuppressWarnings("unchecked")
    public StreamEx<T> read() {
        val gson = new Gson();
        try (val streamReader = new InputStreamReader(getUrl().openStream(), StandardCharsets.UTF_8);
             val jsonReader = new com.google.gson.stream.JsonReader(streamReader)) {
            return StreamEx.of((T[]) Match(new JsonParser().parse(jsonReader)).of(
                    Case($(JsonElement::isJsonArray), j -> gson.fromJson(j, castToArray(entityClass))),
                    Case($(), j -> (T[]) new Object[]{gson.fromJson(j, castToObject(entityClass))})
            ));
        } catch (IOException ex) {
            throw new IllegalArgumentException(
                    format("Unable to read CSV data to %s. Check provided path.", entityClass), ex);
        }
    }
}
