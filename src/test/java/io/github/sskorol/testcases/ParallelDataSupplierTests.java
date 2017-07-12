package io.github.sskorol.testcases;

import io.github.sskorol.dataprovider.DataSupplier;
import io.vavr.Tuple;
import org.testng.annotations.Test;

public class ParallelDataSupplierTests {

    @DataSupplier(runInParallel = true)
    public Tuple getParallelData() {
        return Tuple.of("data1", "data2");
    }

    @DataSupplier
    public Tuple getSeqData() {
        return Tuple.of("data1", "data2");
    }

    @Test(dataProvider = "getParallelData")
    public void supplyParallelData(final String ob) {
        // not implemented
    }

    @Test(dataProvider = "getSeqData")
    public void supplySeqData(final String ob) {
        // not implemented
    }
}
