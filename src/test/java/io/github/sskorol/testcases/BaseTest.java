package io.github.sskorol.testcases;

import io.github.sskorol.dataprovider.DataProviderTransformer;
import io.github.sskorol.listeners.InvokedMethodNameListener;
import org.testng.ITestNGListener;
import org.testng.TestNG;

class BaseTest {

    static InvokedMethodNameListener run(final Class<?>... testClasses) {
        final TestNG tng = create(testClasses);
        final InvokedMethodNameListener listener = new InvokedMethodNameListener();
        final DataProviderTransformer dataProviderTransformer = new DataProviderTransformer();

        tng.addListener((ITestNGListener) listener);
        tng.addListener(dataProviderTransformer);
        tng.setDefaultTestName("DataSupplier tests");
        tng.run();

        return listener;
    }

    private static TestNG create() {
        final TestNG result = new TestNG();
        result.setUseDefaultListeners(false);
        result.setVerbose(0);
        return result;
    }

    private static TestNG create(final Class<?>... testClasses) {
        TestNG result = create();
        result.setTestClasses(testClasses);
        return result;
    }
}