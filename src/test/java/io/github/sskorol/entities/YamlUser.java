package io.github.sskorol.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sskorol.data.Source;

@Source(path = "users.yml")
public class YamlUser {

    @JsonProperty("username")
    private String name;
    private String password;

    public YamlUser() {
    }

    public YamlUser(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var yamlUser = (YamlUser) o;

        if (name != null ? !name.equals(yamlUser.name) : yamlUser.name != null) {
            return false;
        }
        return password != null ? password.equals(yamlUser.password) : yamlUser.password == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "YamlUser("
            + "name=" + name
            + ", password=" + password
            + ")";
    }
}
