package io.github.sskorol.core;

import io.github.sskorol.model.DataSupplierMetaData;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;

import java.util.Collection;
import java.util.Collections;

/**
 * A listener which allows retrieving useful meta-data. Should be implemented on client side, and linked via SPI.
 */
public interface DataSupplierInterceptor {

    default void beforeDataPreparation(final ITestContext context, final ITestNGMethod method) {
        // not implemented
    }

    default void afterDataPreparation(final ITestContext context, final ITestNGMethod method) {
        // not implemented
    }

    default void onDataPreparation(final DataSupplierMetaData metaData) {
        // do nothing
    }

    default Collection<DataSupplierMetaData> getMetaData() {
        return Collections.emptyList();
    }
}
