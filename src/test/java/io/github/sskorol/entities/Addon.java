package io.github.sskorol.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Addon {

    private String organization;
    private Token token;
}
