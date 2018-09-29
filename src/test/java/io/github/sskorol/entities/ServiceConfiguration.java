package io.github.sskorol.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

public class ServiceConfiguration {

    private String image;
    @JsonProperty("network_mode")
    private String networkMode;
    private List<String> expose;
    private List<String> ports;
    private List<String> environment;
    private List<String> links;
    private List<String> volumes;
    private String[] command;

    public ServiceConfiguration() {
    }

    public ServiceConfiguration(final String image, final String networkMode, final List<String> expose,
                                final List<String> ports, final List<String> environment, final List<String> links,
                                final List<String> volumes, final String[] command) {
        this.image = image;
        this.networkMode = networkMode;
        this.expose = expose;
        this.ports = ports;
        this.environment = environment;
        this.links = links;
        this.volumes = volumes;
        this.command = command;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public String getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(final String networkMode) {
        this.networkMode = networkMode;
    }

    public List<String> getExpose() {
        return expose;
    }

    public void setExpose(final List<String> expose) {
        this.expose = expose;
    }

    public List<String> getPorts() {
        return ports;
    }

    public void setPorts(final List<String> ports) {
        this.ports = ports;
    }

    public List<String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(final List<String> environment) {
        this.environment = environment;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(final List<String> links) {
        this.links = links;
    }

    public List<String> getVolumes() {
        return volumes;
    }

    public void setVolumes(final List<String> volumes) {
        this.volumes = volumes;
    }

    public String[] getCommand() {
        return command;
    }

    public void setCommand(final String[] command) {
        this.command = command;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var that = (ServiceConfiguration) o;

        if (image != null ? !image.equals(that.image) : that.image != null) {
            return false;
        }
        if (networkMode != null ? !networkMode.equals(that.networkMode) : that.networkMode != null) {
            return false;
        }
        if (expose != null ? !expose.equals(that.expose) : that.expose != null) {
            return false;
        }
        if (ports != null ? !ports.equals(that.ports) : that.ports != null) {
            return false;
        }
        if (environment != null ? !environment.equals(that.environment) : that.environment != null) {
            return false;
        }
        if (links != null ? !links.equals(that.links) : that.links != null) {
            return false;
        }
        if (volumes != null ? !volumes.equals(that.volumes) : that.volumes != null) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(command, that.command);
    }

    @Override
    public int hashCode() {
        int result = image != null ? image.hashCode() : 0;
        result = 31 * result + (networkMode != null ? networkMode.hashCode() : 0);
        result = 31 * result + (expose != null ? expose.hashCode() : 0);
        result = 31 * result + (ports != null ? ports.hashCode() : 0);
        result = 31 * result + (environment != null ? environment.hashCode() : 0);
        result = 31 * result + (links != null ? links.hashCode() : 0);
        result = 31 * result + (volumes != null ? volumes.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(command);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceConfiguration(" +
            "image=" + image +
            ", networkMode=" + networkMode +
            ", expose=" + expose +
            ", ports=" + ports +
            ", environment=" + environment +
            ", links=" + links +
            ", volumes=" + volumes +
            ", command=" + Arrays.toString(command) +
            ")";
    }
}