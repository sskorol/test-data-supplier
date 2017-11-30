package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

@NoArgsConstructor
@Slf4j
public class IncompleteFactoryTests {

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
