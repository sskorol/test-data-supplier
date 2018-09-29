package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.JsonReader;
import io.github.sskorol.entities.Animal;
import io.github.sskorol.entities.Client;
import io.github.sskorol.entities.JsonUser;
import io.github.sskorol.entities.MissingClient;
import one.util.streamex.StreamEx;
import org.testng.annotations.Test;

import static io.github.sskorol.data.TestDataReader.use;

public class JsonDataSupplierTests {

    @DataSupplier
    public StreamEx<JsonUser> getUsers() {
        return use(JsonReader.class).withTarget(JsonUser.class).read();
    }

    @DataSupplier
    public StreamEx<Client> getClient() {
        return use(JsonReader.class).withTarget(Client.class).read();
    }

    @DataSupplier
    public StreamEx<Animal> getAnimals() {
        return use(JsonReader.class)
            .withTarget(Animal.class)
            .withSource("https://raw.githubusercontent.com/LearnWebCode/json-example/master/animals-1.json")
            .read();
    }

    @DataSupplier
    public StreamEx<MissingClient> getMissingClient() {
        return use(JsonReader.class).withTarget((MissingClient.class)).read();
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
