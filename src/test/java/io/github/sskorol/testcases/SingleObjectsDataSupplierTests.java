package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import io.github.sskorol.entities.User;
import org.testng.annotations.Test;

public class SingleObjectsDataSupplierTests {

    @DataSupplier
    public String getCommonObjectData() {
        return "data1";
    }

    @DataSupplier
    public User getCustomObjectData() {
        return new User("username", "password");
    }

    @DataSupplier
    public long getPrimitiveData() {
        return 5L;
    }

    @Test(dataProvider = "getCommonObjectData")
    public void supplyCommonObject(final String ob) {
        // not implemented
    }

    @Test(dataProvider = "getPrimitiveData")
    public void supplyPrimitiveData(final long ob) {
        // not implemented
    }

    @Test(dataProvider = "getCustomObjectData")
    public void supplyCustomObjectData(final User user) {
        // not implemented
    }

    @Test(dataProviderClass = ExternalDataSuppliers.class, dataProvider = "getExternalObjectData")
    public void supplyExternalObjectData(final double ob) {
        // not implemented
    }
}
