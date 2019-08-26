package io.github.sskorol.entities;

import lombok.Data;

@Data
public class Animal {

    private final String name;
    private final String species;
    private final Food foods;
}
