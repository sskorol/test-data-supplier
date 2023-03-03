package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import io.github.sskorol.entities.User;
import one.util.streamex.StreamEx;
import org.testng.annotations.Test;

public class ArraysDataSupplierTests {

    @DataSupplier(transpose = true)
    public String[] extractCommonArrayData() {
        return new String[] {"data1", "data2"};
    }

    @DataSupplier(flatMap = true)
    public StreamEx extractNestedArrayData() {
        return StreamEx.of("data3", "data4", "data5").map(ob -> new String[] {ob, ob});
    }

    @DataSupplier
    public User[] getCustomArrayData() {
        return new User[] {new User("username", "password"), null};
    }

    @DataSupplier(transpose = true, flatMap = true)
    public User[] getCustomTransposedExtractedArrayData() {
        return new User[] {
            new User("username1", "password1"),
            new User("username2", "password2"),
            null
        };
    }

    @DataSupplier
    public double[] getPrimitiveDoubleArrayData() {
        final double[] doubles = new double[2];
        doubles[0] = 0.1;
        doubles[1] = 0.3;
        return doubles;
    }

    @DataSupplier
    public int[] getPrimitiveIntArrayData() {
        final int[] ints = new int[1];
        ints[0] = 5;
        return ints;
    }

    @DataSupplier
    public long[] getPrimitiveLongArrayData() {
        final long[] longs = new long[3];
        longs[0] = 2L;
        longs[1] = 6L;
        longs[2] = 100L;
        return longs;
    }

    @Test(dataProvider = "extractCommonArrayData")
    public void supplyExtractedArrayData(final String ob1, final String ob2) {
        // not implemented
    }

    @Test(dataProvider = "getPrimitiveDoubleArrayData")
    public void supplyPrimitiveDoubleArrayData(final double ob) {
        // not implemented
    }

    @Test(dataProvider = "getPrimitiveIntArrayData")
    public void supplyPrimitiveIntArrayData(final int ob) {
        // not implemented
    }

    @Test(dataProvider = "getPrimitiveLongArrayData")
    public void supplyPrimitiveLongArrayData(final long ob) {
        // not implemented
    }

    @Test(dataProvider = "getCustomArrayData")
    public void supplyCustomArrayData(final User user) {
        // not implemented
    }

    @Test(dataProvider = "getCustomTransposedExtractedArrayData")
    public void supplyCustomTransposedExtractedArrayData(final User user1, final User user2) {
        // not implemented
    }

    @Test(dataProviderClass = ExternalDataSuppliers.class, dataProvider = "getExternalArrayData")
    public void supplyExternalArrayData(final User user1, final User user2) {
        // not implemented
    }

    @Test(dataProvider = "extractNestedArrayData")
    public void supplyNestedArrayData(final String ob1, final String ob2) {
        // not implemented
    }
}
