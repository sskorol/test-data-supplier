package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.CsvReader;
import io.github.sskorol.model.CrimeRecord;
import io.github.sskorol.model.MissingClient;
import io.github.sskorol.model.User;
import one.util.streamex.StreamEx;
import org.testng.annotations.Test;

import static io.github.sskorol.data.TestDataReader.use;

public class CsvDataSupplierTests {

    @DataSupplier
    public StreamEx<User> getUsers() {
        return use(CsvReader.class)
                .withTarget(User.class)
                .withSource("users.csv")
                .read();
    }

    @DataSupplier
    public StreamEx<CrimeRecord> getCrimes() {
        return use(CsvReader.class)
                .withTarget(CrimeRecord.class)
                .read()
                .limit(1);
    }

    @DataSupplier
    public StreamEx<MissingClient> getMissingClient() {
        return use(CsvReader.class).withTarget(MissingClient.class).read();
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
