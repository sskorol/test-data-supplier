package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import io.github.sskorol.model.User;
import io.vavr.Tuple;
import lombok.val;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.testng.annotations.Test;

import static java.util.Arrays.asList;

public class TupleDataSupplierTests {

    @DataSupplier
    public Tuple getCommonTupleData() {
        return Tuple.of("data1", "data2");
    }

    @DataSupplier(transpose = true)
    public Tuple extractCommonTupleData() {
        return Tuple.of(1, new User("name", "password"));
    }

    @DataSupplier(flatMap = true)
    public StreamEx getInternallyExtractedTupleData() {
        val list1 = asList("data1", "data2");
        val list2 = asList("data3", "data4", "data5");
        val minSize = Math.min(list1.size(), list2.size());

        return EntryStream.of(list1)
                          .limit(minSize)
                          .mapKeyValue((i, val) -> Tuple.of(val, list2.get(i)));
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

    @Test(dataProvider = "getInternallyExtractedTupleData")
    public void supplyInternallyExtractedTupleData(final String ob1, final String ob2) {
        // not implemented
    }

}
