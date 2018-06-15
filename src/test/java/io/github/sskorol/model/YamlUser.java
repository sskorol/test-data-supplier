package io.github.sskorol.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sskorol.data.Source;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Source(path = "users.yml")
public class YamlUser {

    @JsonProperty("username")
    private String name;
    private String password;
}
