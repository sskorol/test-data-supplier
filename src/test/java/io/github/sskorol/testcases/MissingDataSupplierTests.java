package io.github.sskorol.testcases;

import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import org.testng.annotations.Test;

public class MissingDataSupplierTests {

    @Test(dataProvider = "missingDataSupplier")
    public void failOnDataSupplying() {
        // not implemented
    }

    @Test(dataProviderClass = ExternalDataSuppliers.class, dataProvider = "missingExternalDataSupplier")
    public void failOnExternalDataSupplying() {
        // not implemented
    }
}
