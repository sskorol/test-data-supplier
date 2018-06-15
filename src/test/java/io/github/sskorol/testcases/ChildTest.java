package io.github.sskorol.testcases;

import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import org.testng.annotations.Test;

@Test(dataProviderClass = ExternalDataSuppliers.class)
public class ChildTest extends ParentTest {

    @Test
    public void dummyTest() {
        // not important
    }
}
