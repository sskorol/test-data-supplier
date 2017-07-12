package io.github.sskorol.testcases;

import io.github.sskorol.dataprovider.DataSupplier;
import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import io.github.sskorol.model.User;
import io.vavr.Tuple;
import org.testng.annotations.Test;

public class TupleDataSupplierTests {

    @DataSupplier
    public Tuple getCommonTupleData() {
        return Tuple.of("data1", "data2");
    }

    @DataSupplier(extractValues = true)
    public Tuple extractCommonTupleData() {
        return Tuple.of(1, new User("name", "password"));
    }

    @Test(dataProvider = "getCommonTupleData")
    public void supplyCommonTupleData(final String ob) {
        // not implemented
    }

    @Test(dataProvider = "extractCommonTupleData")
    public void supplyExtractedTupleData(final int ob, final User user) {
        // not implemented
    }

    @Test(dataProviderClass = ExternalDataSuppliers.class, dataProvider = "getExternalTupleData")
    public void supplyExternalTupleData(final String ob1, final int ob2, final double ob3) {
        // not implemented
    }
}
