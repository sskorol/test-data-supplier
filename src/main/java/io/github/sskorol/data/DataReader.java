package io.github.sskorol.data;

import one.util.streamex.StreamEx;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static io.github.sskorol.utils.ReflectionUtils.getSourcePath;

public interface DataReader<T> {

    StreamEx<T> read();

    Class<T> getEntityClass();

    String getPath();

    default DataReader<T> additionalSources(final List<String> names) {
        return this;
    }

    default URL getUrl() throws IOException {
        return getPath().isEmpty() ? getSourcePath(getEntityClass()) : getSourcePath(getPath());
    }
}
