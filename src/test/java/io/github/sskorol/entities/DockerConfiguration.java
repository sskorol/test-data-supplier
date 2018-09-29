package io.github.sskorol.entities;

import io.github.sskorol.data.Source;

import java.util.Map;

@Source(path = "docker-compose.yml")
public class DockerConfiguration {

    private String version;
    private Map<String, ServiceConfiguration> services;

    public DockerConfiguration() {
    }

    public DockerConfiguration(final String version, final Map<String, ServiceConfiguration> services) {
        this.version = version;
        this.services = services;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public Map<String, ServiceConfiguration> getServices() {
        return services;
    }

    public void setServices(final Map<String, ServiceConfiguration> services) {
        this.services = services;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var that = (DockerConfiguration) o;

        if (version != null ? !version.equals(that.version) : that.version != null) {
            return false;
        }
        return services != null ? services.equals(that.services) : that.services == null;
    }

    @Override
    public int hashCode() {
        int result = version != null ? version.hashCode() : 0;
        result = 31 * result + (services != null ? services.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DockerConfiguration(" +
            "version=" + version +
            ", services=" + services +
            ")";
    }
}
