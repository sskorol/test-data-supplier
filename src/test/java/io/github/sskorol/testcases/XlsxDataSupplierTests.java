package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.XlsxReader;
import io.github.sskorol.entities.*;
import one.util.streamex.StreamEx;
import org.testng.annotations.Test;

import static io.github.sskorol.data.TestDataReader.use;

public class XlsxDataSupplierTests {

    @DataSupplier
    public StreamEx<PersonWithoutSheet> getPersonsWithoutSheet() {
        return use(XlsxReader.class).withTarget(PersonWithoutSheet.class).read();
    }

    @DataSupplier
    public StreamEx<PersonWithSheet> getPersonsWithSheet() {
        return use(XlsxReader.class).withTarget(PersonWithSheet.class).read();
    }

    @DataSupplier
    public StreamEx<TestData> getDataWithCustomConverters() {
        return use(XlsxReader.class).withTarget(TestData.class).read();
    }

    @Test(dataProvider = "getPersonsWithoutSheet")
    public void shouldReadLocalExcelSpreadsheetWithoutSheet(final PersonWithoutSheet person) {
        // not implemented
    }

    @Test(dataProvider = "getPersonsWithSheet")
    public void shouldReadLocalExcelSpreadsheetWithSheet(final PersonWithSheet person) {
        // not implemented
    }

    @Test(dataProvider = "getDataWithCustomConverters")
    public void shouldReadLocalExcelSpreadsheetWithCustomConverters(final TestData testData){
        // not implemented
    }
}
