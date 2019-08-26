package io.github.sskorol.entities;

import io.github.sskorol.data.Source;
import lombok.Data;

@Source(path = "client.json")
@Data
public class Client {

    private final String firstName;
    private final String lastName;
}
