package io.github.sskorol.model;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.utils.ReflectionUtils;
import lombok.Getter;
import lombok.val;
import one.util.streamex.*;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;

import java.lang.reflect.Method;
import java.util.List;

import static io.github.sskorol.utils.ReflectionUtils.invokeDataSupplier;
import static io.github.sskorol.utils.ReflectionUtils.wrap;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

/**
 * Base container for DataSupplier meta data.
 */
@SuppressWarnings("FinalLocalVariable")
public class DataSupplierMetaData {

    @Getter
    private final List<Object[]> testData;
    private final boolean transpose;
    private final boolean flatMap;
    private final int[] indices;
    private final TestNGMetaData metaData;

    public DataSupplierMetaData(final ITestContext context, final ITestNGMethod testMethod) {
        this.metaData = new TestNGMetaData(context, testMethod);
        this.transpose = metaData.getDataSupplierArg(DataSupplier::transpose, false);
        this.flatMap = metaData.getDataSupplierArg(DataSupplier::flatMap, false);
        this.indices = metaData.getDataSupplierArg(DataSupplier::indices, new int[0]);
        this.testData = transform();
    }

    public ITestNGMethod getTestMethod() {
        return metaData.getTestMethod();
    }

    public Method getDataSupplierMethod() {
        return metaData.getDataSupplierMethod();
    }

    private List<Object[]> transform() {
        val data = wrap(obtainReturnValue()).toList();
        val indicesList = indicesList(data.size());
        val wrappedReturnValue = EntryStream.of(data).filterKeys(indicesList::contains).values();

        if (transpose) {
            return singletonList(flatMap
                    ? wrappedReturnValue.flatMap(ReflectionUtils::wrap).toArray()
                    : wrappedReturnValue.toArray());
        }

        return wrappedReturnValue.map(ob -> flatMap ? wrap(ob).toArray() : new Object[]{ob}).toList();
    }

    private Object obtainReturnValue() {
        return invokeDataSupplier(metaData.getDataSupplierMetaData());
    }

    private List<Integer> indicesList(final int collectionSize) {
        return ofNullable(indices)
                .filter(indicesArray -> indicesArray.length > 0)
                .map(IntStreamEx::of)
                .orElseGet(() -> IntStreamEx.range(0, collectionSize))
                .boxed()
                .toList();
    }
}
