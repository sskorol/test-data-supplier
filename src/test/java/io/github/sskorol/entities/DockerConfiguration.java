package io.github.sskorol.entities;

import io.github.sskorol.data.Source;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Source(path = "docker-compose.yml")
@Data
@NoArgsConstructor
public class DockerConfiguration {

    private String version;
    private Map<String, ServiceConfiguration> services;
}
