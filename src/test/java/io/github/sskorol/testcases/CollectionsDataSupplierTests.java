package io.github.sskorol.testcases;

import io.github.sskorol.dataprovider.DataSupplier;
import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import io.github.sskorol.model.User;
import one.util.streamex.StreamEx;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

public class CollectionsDataSupplierTests {

    @DataSupplier
    public List<String> getCommonListData() {
        return asList("data1", "data2");
    }

    @DataSupplier(extractValues = true)
    public Set<User> extractCustomListData() {
        return StreamEx.of(
                new User("username", "password"),
                new User("username", "password"),
                null,
                null).toSet();
    }

    @Test(dataProvider = "getCommonListData")
    public void supplyCommonListData(final String ob) {
        // not implemented
    }

    @Test(dataProvider = "extractCustomListData")
    public void supplyCustomListData(final User... users) {
        // not implemented
    }

    @Test(dataProviderClass = ExternalDataSuppliers.class, dataProvider = "getExternalCollectionData")
    public void supplyExternalCollectionData(final String ob) {
        // not implemented
    }
}
