package io.github.sskorol.entities;

import com.google.gson.annotations.SerializedName;
import io.github.sskorol.data.Source;

@Source(path = "users.json")
public class JsonUser {

    @SerializedName("username")
    private final String name;
    private final String password;

    public JsonUser(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var jsonUser = (JsonUser) o;

        if (name != null ? !name.equals(jsonUser.name) : jsonUser.name != null) {
            return false;
        }
        return password != null ? password.equals(jsonUser.password) : jsonUser.password == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JsonUser(" +
            "name=" + name +
            ", password=" + password +
            ")";
    }
}
