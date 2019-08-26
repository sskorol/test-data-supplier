package io.github.sskorol.entities;

import io.github.sskorol.data.FieldName;
import lombok.Data;

@Data
public class User {

    @FieldName("username")
    private final String name;
    private final String password;
}
