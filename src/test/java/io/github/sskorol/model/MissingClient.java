package io.github.sskorol.model;

import io.github.sskorol.data.Source;
import lombok.Data;

@Data
@Source(path = "unknown")
public class MissingClient {

    private final String firstName;
    private final String lastName;
}
