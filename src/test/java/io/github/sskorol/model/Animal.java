package io.github.sskorol.model;

import lombok.Data;

@Data
@Source(path = "https://raw.githubusercontent.com/LearnWebCode/json-example/master/animals-1.json")
public class Animal {

    private final String name;
    private final String species;
    private final Food foods;
}
