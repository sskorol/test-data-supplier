package io.github.sskorol.model;

import io.vavr.Tuple;
import one.util.streamex.*;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Internal structure for handling type transformations.
 */
public enum TypeMappings {

    COLLECTION(Collection.class, d -> StreamEx.of((Collection<?>) d)),
    MAP(Map.class, d -> EntryStream.of((Map<?, ?>) d).mapKeyValue(AbstractMap.SimpleEntry::new)),
    ENTRY(Map.Entry.class, d -> StreamEx.of(((Map.Entry) d).getKey(), ((Map.Entry) d).getValue())),
    OBJECT_ARRAY(Object[].class, d -> StreamEx.of((Object[]) d)),
    DOUBLE_ARRAY(double[].class, d -> DoubleStreamEx.of((double[]) d).boxed()),
    INT_ARRAY(int[].class, d -> IntStreamEx.of((int[]) d).boxed()),
    LONG_ARRAY(long[].class, d -> LongStreamEx.of((long[]) d).boxed()),
    STREAM(Stream.class, d -> StreamEx.of((Stream<?>) d)),
    TUPLE(Tuple.class, d -> StreamEx.of(((Tuple) d).toSeq().toJavaArray()));

    private final Class<?> typeClass;
    private final Function<Object, StreamEx<?>> mapper;

    TypeMappings(final Class<?> typeClass, final Function<Object, StreamEx<?>> mapper) {
        this.typeClass = typeClass;
        this.mapper = mapper;
    }

    public boolean isInstanceOf(final Object ob) {
        return ob != null && typeClass.isAssignableFrom(ob.getClass());
    }

    @SuppressWarnings("unchecked")
    public <T> StreamEx<T> streamOf(final T ob) {
        return (StreamEx<T>) mapper.apply(ob);
    }
}
