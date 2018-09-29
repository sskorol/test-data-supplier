package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import io.github.sskorol.entities.User;
import org.testng.annotations.Test;

public class DataSupplierWithCustomNamesTests extends BaseDataSupplier {

    @DataSupplier(name = "User supplier")
    public User getDataWithCustomName() {
        return new User("userFromNamedDataSupplier", "password");
    }

    @Test(dataProvider = "User supplier")
    public void supplyUserFromNamedDataSupplier(final User user) {
        // not implemented
    }

    @Test(dataProviderClass = ExternalDataSuppliers.class, dataProvider = "Password supplier")
    public void supplyExternalPasswordFromNamedDataSupplier(final String password) {
        // not implemented
    }

    @Test(dataProvider = "Parent class data")
    public void supplyStringFromNamedParentClassDataSupplier(final String ob) {
        // not implemented
    }
}
