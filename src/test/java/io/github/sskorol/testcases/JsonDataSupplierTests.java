package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.model.Animal;
import io.github.sskorol.model.Client;
import io.github.sskorol.model.JsonUser;
import io.github.sskorol.model.MissingClient;
import one.util.streamex.StreamEx;
import org.testng.annotations.Test;

import static io.github.sskorol.utils.DataSourceUtils.getJsonRecords;

public class JsonDataSupplierTests {

    @DataSupplier
    public StreamEx<JsonUser> getUsers() {
        return getJsonRecords(JsonUser.class);
    }

    @DataSupplier
    public StreamEx<Client> getClient() {
        return getJsonRecords(Client.class);
    }

    @DataSupplier
    public StreamEx<Animal> getAnimals() {
        return getJsonRecords(Animal.class);
    }

    @DataSupplier
    public StreamEx<MissingClient> getMissingClient() {
        return getJsonRecords(MissingClient.class);
    }

    @Test(dataProvider = "getUsers")
    public void shouldReadLocalJsonArray(final JsonUser user) {
        // not implemented
    }

    @Test(dataProvider = "getClient")
    public void shouldReadLocalJson(final Client client) {
        // not implemented
    }

    @Test(dataProvider = "getAnimals")
    public void shouldReadRemoteJson(final Animal animal) {
        // not implemented
    }

    @Test(dataProvider = "getMissingClient")
    public void shouldNotBeExecutedWithMissingJsonResource(final MissingClient missingClient) {
        // not implemented
    }
}
