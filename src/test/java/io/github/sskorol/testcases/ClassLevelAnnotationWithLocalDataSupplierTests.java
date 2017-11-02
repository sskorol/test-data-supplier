package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import org.testng.annotations.Test;

@Test
public class ClassLevelAnnotationWithLocalDataSupplierTests {

    @DataSupplier
    public String getClassLevelLocalData() {
        return "data";
    }

    public void shouldBeExecutedWithClassLevelAnnotationWithoutDataSupplier() {
        // not implemented
    }

    @Test(dataProvider = "getClassLevelLocalData")
    public void shouldBeExecutedWithLocalDataSupplier(final String ob) {
        // not implemented
    }
}
