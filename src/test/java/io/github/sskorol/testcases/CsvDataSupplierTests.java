package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.model.CrimeRecord;
import io.github.sskorol.model.MissingClient;
import io.github.sskorol.model.User;
import one.util.streamex.StreamEx;
import org.testng.annotations.Test;

import static io.github.sskorol.utils.DataSourceUtils.getCsvRecords;

public class CsvDataSupplierTests {

    @DataSupplier
    public StreamEx<User> getUsers() {
        return getCsvRecords(User.class);
    }

    @DataSupplier
    public StreamEx<CrimeRecord> getCrimes() {
        return getCsvRecords(CrimeRecord.class).limit(1);
    }

    @DataSupplier
    public StreamEx<MissingClient> getMissingClient() {
        return getCsvRecords(MissingClient.class);
    }

    @Test(dataProvider = "getUsers")
    public void shouldReadLocalCsv(final User user) {
        // not implemented
    }

    @Test(dataProvider = "getCrimes")
    public void shouldReadRemoteCsv(final CrimeRecord crimeRecord) {
        // not implemented
    }

    @Test(dataProvider = "getMissingClient")
    public void shouldNotBeExecutedWithMissingCsvResource(final MissingClient missingClient) {
        // not implemented
    }
}
