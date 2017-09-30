package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import io.github.sskorol.model.User;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;

public class CollectionsDataSupplierTests {

    @DataSupplier
    public List<String> getCommonListData() {
        return asList("data1", "data2");
    }

    @DataSupplier
    public Map<Integer, String> getCommonMapData() {
        return EntryStream.of(asList("user1", "user2")).toMap();
    }

    @DataSupplier(flatMap = true)
    public Map<Integer, String> getInternallyExtractedMapData() {
        return EntryStream.of(asList("user3", "user4")).toMap();
    }

    @DataSupplier(transpose = true)
    public Map<Integer, String> getTransposedMapData() {
        return EntryStream.of(asList("user5", "user6")).toMap();
    }

    @DataSupplier(transpose = true, flatMap = true)
    public Map<Integer, String> getTransposedInternallyExtractedMapData() {
        return EntryStream.of(asList("user7", "user8")).toMap();
    }

    @DataSupplier(transpose = true)
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

    @Test(dataProvider = "getCommonMapData")
    public void supplyCommonMapData(final Map.Entry<Integer, String> ob) {
        // not implemented
    }

    @Test(dataProvider = "getInternallyExtractedMapData")
    public void supplyInternallyExtractedMapData(final Integer key, final String value) {
        // not implemented
    }

    @Test(dataProvider = "getTransposedMapData")
    public void supplyTransposedMapData(final Map.Entry<Integer, String> ob1, final Map.Entry<Integer, String> ob2) {
        // not implemented
    }

    @Test(dataProvider = "getTransposedInternallyExtractedMapData")
    public void supplyTransposedInternallyExtractedMapData(final Integer index1, final String ob1,
                                                           final Integer index2, final String ob2) {
        // not implemented
    }
}
