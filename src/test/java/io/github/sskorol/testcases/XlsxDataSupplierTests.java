package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.XlsxReader;
import io.github.sskorol.entities.*;
import one.util.streamex.StreamEx;
import org.testng.annotations.Test;

import static io.github.sskorol.data.TestDataReader.use;

public class XlsxDataSupplierTests {

    @DataSupplier
    public StreamEx<MissingClient> getMissingClient() {
        return use(XlsxReader.class).withTarget(MissingClient.class).read();
    }

    @DataSupplier(propagateTestFailure = true)
    public StreamEx<PersonWithoutSheet> getPersonsWithoutSheet() {
        return use(XlsxReader.class).withTarget(PersonWithoutSheet.class).read();
    }

    @DataSupplier(propagateTestFailure = true)
    public StreamEx<PersonWithSheet> getPersonsWithSheet() {
        return use(XlsxReader.class).withTarget(PersonWithSheet.class).read();
    }

    @DataSupplier(propagateTestFailure = true)
    public StreamEx<XlsxData> getDataWithCustomConverters() {
        return use(XlsxReader.class).withTarget(XlsxData.class).read();
    }

    @DataSupplier(propagateTestFailure = true)
    public StreamEx<XlsxData> getDataWithCustomSheets() {
        return use(XlsxReader.class)
            .withTarget(XlsxData.class)
            .withAdditionalSources("Sheet2", "Sheet3")
            .read();
    }

    @Test(dataProvider = "getPersonsWithoutSheet")
    public void shouldReadLocalExcelSpreadsheetWithoutSheet(final PersonWithoutSheet person) {
        // not implemented
    }

    @Test(dataProvider = "getPersonsWithSheet")
    public void shouldReadLocalExcelSpreadsheetWithSheet(final PersonWithSheet person) {
        // not implemented
    }

    @Test(dataProvider = "getMissingClient")
    public void shouldNotBeExecutedWithMissingXlsxResource(final MissingClient missingClient) {
        // not implemented
    }

    @Test(dataProvider = "getDataWithCustomConverters")
    public void shouldReadLocalExcelSpreadsheetWithCustomConverters(final XlsxData xlsxData) {
        // not implemented
    }

    @Test(dataProvider = "getDataWithCustomSheets")
    public void shouldReadLocalExcelSpreadsheetWithCustomSheets(final XlsxData xlsxData) {
        // not implemented
    }
}
