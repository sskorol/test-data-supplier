package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import org.testng.annotations.Test;

public class DataSupplierWithErrorPropagationTests {

    @DataSupplier(propagateTestFailure = true)
    public String dataWhichThrowsExceptionWithPropagation() {
        throw new IllegalStateException("Failing the test");
    }

    @DataSupplier
    public String dataWhichThrowsExceptionWithoutPropagation() {
        throw new IllegalStateException("Skipping the test");
    }

    @Test(dataProvider = "dataWhichThrowsExceptionWithPropagation")
    public void shouldPropagateTestFailure(final String ob) {
        // not implemented
    }

    @Test(dataProvider = "dataWhichThrowsExceptionWithoutPropagation")
    public void shouldSkipTest(final String ob) {
        // not implemented
    }
}
