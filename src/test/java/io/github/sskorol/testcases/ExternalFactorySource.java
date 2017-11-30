package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import org.testng.annotations.Factory;

import java.util.List;

import static java.util.Arrays.asList;

public class ExternalFactorySource {

    @DataSupplier
    public List sourceData() {
        return asList("data1", "data2");
    }

    @Factory(dataProvider = "sourceData", dataProviderClass = ExternalFactorySource.class)
    public Object[] createTestClassInstance(final String data) {
        return new Object[]{new ExternalFactoryTestTarget(data)};
    }
}
