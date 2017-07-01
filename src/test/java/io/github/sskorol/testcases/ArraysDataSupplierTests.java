package io.github.sskorol.testcases;

import io.github.sskorol.dataprovider.DataSupplier;
import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import io.github.sskorol.model.User;
import org.testng.annotations.Test;

public class ArraysDataSupplierTests {

    @DataSupplier(extractValues = true)
    public String[] extractCommonArrayData() {
        return new String[]{"data1", "data2"};
    }

    @DataSupplier
    public User[] getCustomArrayData() {
        return new User[]{new User("username", "password"), null};
    }

    @DataSupplier
    public double[] getPrimitiveArrayData() {
        final double[] doubles = new double[2];
        doubles[0] = 0.1;
        doubles[1] = 0.3;
        return doubles;
    }

    @Test(dataProvider = "extractCommonArrayData")
    public void supplyExtractedArrayData(final String ob1, final String ob2) {
        // not implemented
    }

    @Test(dataProvider = "getPrimitiveArrayData")
    public void supplyPrimitiveArrayData(final double ob) {
        // not implemented
    }

    @Test(dataProvider = "getCustomArrayData")
    public void supplyCustomArrayData(final User user) {
        // not implemented
    }

    @Test(dataProviderClass = ExternalDataSuppliers.class, dataProvider = "getExternalArrayData")
    public void supplyExternalArrayData(final User user1, final User user2) {
        // not implemented
    }
}
