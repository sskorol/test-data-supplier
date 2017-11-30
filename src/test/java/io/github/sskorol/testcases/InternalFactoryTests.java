package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import lombok.NoArgsConstructor;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

@NoArgsConstructor
public class InternalFactoryTests {

    @BeforeMethod
    public void setUp() {
        // not implemented
    }

    @DataSupplier
    public StreamEx getConstructorData() {
        return IntStreamEx.rangeClosed(1, 3).boxed();
    }

    @DataSupplier
    public String getTestData() {
        return "data";
    }

    @Factory(dataProvider = "getConstructorData", dataProviderClass = InternalFactoryTests.class)
    public InternalFactoryTests(final int index) {
        // not implemented
    }

    @Test(dataProvider = "getTestData")
    public void internalFactoryTest(final String data) {
        // not implemented
    }
}
