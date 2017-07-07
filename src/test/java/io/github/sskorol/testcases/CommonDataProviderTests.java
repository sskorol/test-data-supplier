package io.github.sskorol.testcases;

import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.stream.Stream;

public class CommonDataProviderTests {

    @DataProvider
    public Iterator<Object[]> getData() {
        return Stream.of("data").map(d -> new Object[]{d}).iterator();
    }

    @Test
    public void shouldPassWithoutDataProvider() {
        // not implemented
    }

    @Test(dataProvider = "getData")
    public void shouldPassWithCommonDataProvider(final String ob) {
        // not implemented
    }

    @Test(dataProviderClass = ExternalDataSuppliers.class, dataProvider = "getCommonData")
    public void shouldPassWithExternalDataProvider(final String ob) {
        // not implemented
    }
}
