package io.github.sskorol.testcases;

import org.testng.annotations.Test;

public abstract class ParentTest {

    @Test(dataProvider = "externalHashes")
    public void provideDataFromParentClassDataSupplier(final String hash, final String password) {
        // not important
    }
}
