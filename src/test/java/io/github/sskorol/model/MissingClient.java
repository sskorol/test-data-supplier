package io.github.sskorol.model;

import lombok.Data;

@Data
@Source(path = "unknown")
public class MissingClient {

    private final String firstName;
    private final String lastName;
}
