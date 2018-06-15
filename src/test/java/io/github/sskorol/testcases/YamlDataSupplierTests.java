package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.YamlReader;
import io.github.sskorol.model.*;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.testng.annotations.Test;

import static io.github.sskorol.data.TestDataReader.use;

@Slf4j
public class YamlDataSupplierTests {

    @DataSupplier
    public StreamEx<DockerConfiguration> getDockerConfiguration() {
        return use(YamlReader.class).withTarget(DockerConfiguration.class).read();
    }

    @DataSupplier
    public StreamEx<YamlUser> getUsers() {
        return use(YamlReader.class).withTarget(YamlUser.class).read();
    }

    @DataSupplier
    public StreamEx<TravisConfiguration> getTravisConfiguration() {
        return use(YamlReader.class).withTarget(TravisConfiguration.class).read();
    }

    @DataSupplier
    public StreamEx<MissingClient> getMissingClient() {
        return use(YamlReader.class).withTarget(MissingClient.class).read();
    }

    @Test(dataProvider = "getDockerConfiguration")
    public void shouldReadSingleLocalYamlData(final DockerConfiguration configuration) {
        // not implemented
    }

    @Test(dataProvider = "getUsers")
    public void shouldReadMultiLocalYamlData(final YamlUser user) {
        // not implemented
    }

    @Test(dataProvider = "getTravisConfiguration")
    public void shouldReadRemoteYamlData(final TravisConfiguration configuration) {
        // not implemented
    }

    @Test(dataProvider = "getMissingClient")
    public void shouldNotBeExecutedWithMissingYamlResource(final MissingClient missingClient) {
        // not implemented
    }
}
