package io.github.sskorol.model;

import com.google.gson.annotations.SerializedName;
import io.github.sskorol.csv.Source;
import lombok.Data;

@Data
@Source(path = "users.json")
public class JsonUser {

    @SerializedName("username")
    private final String name;
    private final String password;
}
