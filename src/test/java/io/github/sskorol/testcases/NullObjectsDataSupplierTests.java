package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.model.User;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Stream;

public class NullObjectsDataSupplierTests {

    @DataSupplier(extractValues = true)
    public String extractNullObjectData() {
        return null;
    }

    @DataSupplier
    public User getNullObjectData() {
        return null;
    }

    @DataSupplier
    public List<String> getNullCollectionData() {
        return null;
    }

    @DataSupplier
    public User[] getNullArrayData() {
        return null;
    }

    @DataSupplier(extractValues = true)
    public Stream<String> getNullStreamData() {
        return null;
    }

    @Test(dataProvider = "extractNullObjectData")
    public void supplyExtractedNullObject(final String ob) {
        // not implemented
    }

    @Test(dataProvider = "getNullObjectData")
    public void supplyNullObjectData(final User user) {
        // not implemented
    }

    @Test(dataProvider = "getNullArrayData")
    public void supplyNullArrayData(final String ob) {
        // not implemented
    }

    @Test(dataProvider = "getNullCollectionData")
    public void supplyNullCollectionData(final User user) {
        // not implemented
    }

    @Test(dataProvider = "getNullStreamData")
    public void supplyNullStreamData(final String... objects) {
        // not implemented
    }
}
