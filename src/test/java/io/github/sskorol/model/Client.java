package io.github.sskorol.model;

import io.github.sskorol.csv.Source;
import lombok.Data;

@Data
@Source(path = "client.json")
public class Client {

    private final String firstName;
    private final String lastName;
}
