package io.github.sskorol.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import lombok.experimental.UtilityClass;
import lombok.val;
import one.util.streamex.StreamEx;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static io.github.sskorol.utils.ReflectionUtils.castToArray;
import static io.github.sskorol.utils.ReflectionUtils.castToObject;
import static io.github.sskorol.utils.ReflectionUtils.getSourcePath;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static java.lang.String.format;
import static org.apache.commons.csv.CSVParser.parse;
import static org.joor.Reflect.on;

/**
 * CSV and JSON data processing class.
 */
@UtilityClass
@SuppressWarnings("FinalLocalVariable")
public class DataSourceUtils {

    @SuppressWarnings("unchecked")
    public <T> StreamEx<T> getJsonRecords(final Class<T> entity) {
        val gson = new Gson();
        try (val streamReader = new InputStreamReader(getSourcePath(entity).openStream(), StandardCharsets.UTF_8);
             val jsonReader = new JsonReader(streamReader)) {
            return StreamEx.of((T[]) Match(new JsonParser().parse(jsonReader)).of(
                    Case($(JsonElement::isJsonArray), j -> gson.fromJson(j, castToArray(entity))),
                    Case($(), j -> (T[]) new Object[]{gson.fromJson(j, castToObject(entity))})
            ));
        } catch (IOException ex) {
            throw new IllegalArgumentException(
                    format("Unable to read CSV data to %s. Check provided path.", entity), ex);
        }
    }

    public static <T> StreamEx<T> getCsvRecords(final Class<T> entity) {
        try (val csvParser = parse(getSourcePath(entity), StandardCharsets.UTF_8,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            val entityFields = StreamEx.of(entity.getDeclaredFields()).map(ReflectionUtils::getFieldName).toList();
            return StreamEx.of(csvParser.getRecords())
                           .map(record -> StreamEx.of(entityFields).map(record::get).toArray())
                           .map(args -> on(entity).create(args).get());
        } catch (IOException ex) {
            throw new IllegalArgumentException(
                    format("Unable to read JSON data to %s. Check provided path.", entity), ex);
        }
    }
}
