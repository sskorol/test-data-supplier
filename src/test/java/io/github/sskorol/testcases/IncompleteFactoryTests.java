package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class IncompleteFactoryTests {

    public IncompleteFactoryTests() {
    }

    @DataSupplier
    public StreamEx getIncompleteConstructorData() {
        return IntStreamEx.rangeClosed(0, 10).filter(i -> i % 2 != 0).boxed();
    }

    @Factory(dataProvider = "getIncompleteConstructorData")
    public IncompleteFactoryTests(final int index) {
        // not implemented
    }

    @Test
    public void incompleteFactoryTest() {
        // not implemented
    }
}
