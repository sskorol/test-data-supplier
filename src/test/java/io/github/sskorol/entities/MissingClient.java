package io.github.sskorol.entities;

import io.github.sskorol.data.Source;
import lombok.Data;

@Source(path = "unknown")
@Data
public class MissingClient {

    private final String firstName;
    private final String lastName;
}
