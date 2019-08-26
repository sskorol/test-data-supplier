package io.github.sskorol.entities;

import com.google.gson.annotations.SerializedName;
import io.github.sskorol.data.Source;
import lombok.Data;

@Source(path = "users.json")
@Data
public class JsonUser {

    @SerializedName("username")
    private final String name;
    private final String password;
}
