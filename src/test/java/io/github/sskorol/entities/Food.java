package io.github.sskorol.entities;

import lombok.Data;

@Data
public class Food {

    private final String[] likes;
    private final String[] dislikes;
}