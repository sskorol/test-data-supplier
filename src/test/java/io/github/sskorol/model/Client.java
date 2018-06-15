package io.github.sskorol.model;

import io.github.sskorol.data.Source;
import lombok.Data;

@Data
@Source(path = "client.json")
public class Client {

    private final String firstName;
    private final String lastName;
}
