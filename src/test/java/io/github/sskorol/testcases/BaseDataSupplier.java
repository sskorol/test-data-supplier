package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;

public class BaseDataSupplier {

    @DataSupplier(name = "Parent class data")
    public String getBaseData() {
        return "baseData";
    }
}
