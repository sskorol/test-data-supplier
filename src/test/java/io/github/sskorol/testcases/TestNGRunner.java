package io.github.sskorol.testcases;

import io.github.sskorol.core.DataProviderTransformer;
import io.github.sskorol.core.InvokedMethodNameListener;
import org.testng.ITestNGListener;
import org.testng.TestNG;

class TestNGRunner {

    static InvokedMethodNameListener run(final Class<?>... testClasses) {
        return run(InvokedMethodNameListener.class.getName(), testClasses);
    }

    @SuppressWarnings("unchecked")
    static <T> T run(final String listenerClass, final Class<?>... testClasses) {
        final TestNG tng = create(testClasses);
        final InvokedMethodNameListener listener = new InvokedMethodNameListener();
        final DataProviderTransformer dataProviderTransformer = new DataProviderTransformer();

        tng.addListener((ITestNGListener) listener);
        tng.setDefaultTestName("DataSupplier tests");
        tng.run();

        return (T) (listenerClass.equals(InvokedMethodNameListener.class.getName()) ? listener : dataProviderTransformer);
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