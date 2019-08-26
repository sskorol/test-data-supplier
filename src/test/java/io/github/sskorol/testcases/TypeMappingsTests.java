package io.github.sskorol.testcases;

import io.github.sskorol.model.TypeMappings;
import io.vavr.Tuple;
import one.util.streamex.StreamEx;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeMappingsTests {

    @Test
    public void providedObjectsShouldMatchMappedInstances() {
        assertThat(TypeMappings.COLLECTION.isInstanceOf(List.of("data1", "data2"))).as("Collection").isTrue();
        assertThat(TypeMappings.MAP.isInstanceOf(Map.of(1, "data1"))).as("Map").isTrue();
        assertThat(TypeMappings.ENTRY.isInstanceOf(Map.entry(1, "data1"))).as("Entry").isTrue();
        assertThat(TypeMappings.OBJECT_ARRAY.isInstanceOf(StreamEx.of("data1", "data2").toArray())).as("Object[]").isTrue();
        assertThat(TypeMappings.DOUBLE_ARRAY.isInstanceOf(new double[]{0.1, 0.2})).as("Double[]").isTrue();
        assertThat(TypeMappings.INT_ARRAY.isInstanceOf(new int[]{1, 2})).as("int[]").isTrue();
        assertThat(TypeMappings.LONG_ARRAY.isInstanceOf(new long[]{1, 2})).as("long[]").isTrue();
        assertThat(TypeMappings.STREAM.isInstanceOf(StreamEx.of("data1", "data2"))).as("Stream").isTrue();
        assertThat(TypeMappings.TUPLE.isInstanceOf(Tuple.of(1, "data1"))).as("Tuple").isTrue();
        assertThat(TypeMappings.COLLECTION.isInstanceOf(null)).isFalse();
    }
}
