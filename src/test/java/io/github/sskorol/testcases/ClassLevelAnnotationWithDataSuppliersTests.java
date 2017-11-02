package io.github.sskorol.testcases;

import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import org.testng.annotations.Test;

@Test(dataProviderClass = ExternalDataSuppliers.class, dataProvider = "getClassLevelGlobalData")
public class ClassLevelAnnotationWithDataSuppliersTests {

    public void shouldBeExecutedWithClassLevelAnnotationWithDataSupplier(final String ob) {
        // not implemented
    }

    @Test(dataProviderClass = ExternalDataSuppliers.class, dataProvider = "getClassLevelLocalData")
    public void shouldBeExecutedWithExternalDataSupplier(final String ob) {
        // not implemented
    }
}
