package io.github.sskorol.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Addon {

    private String organization;
    private Token token;
}
