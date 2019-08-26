package io.github.sskorol.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
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
}