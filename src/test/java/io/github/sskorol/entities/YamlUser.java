package io.github.sskorol.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sskorol.data.Source;
import lombok.Data;
import lombok.NoArgsConstructor;

@Source(path = "users.yml")
@Data
@NoArgsConstructor
public class YamlUser {

    @JsonProperty("username")
    private String name;
    private String password;
}
