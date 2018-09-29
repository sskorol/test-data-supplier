package io.github.sskorol.entities;

import io.github.sskorol.data.FieldName;

public class User {

    @FieldName("username")
    private final String name;
    private final String password;

    public User(final String name, final String password) {
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

        var user = (User) o;

        return name.equals(user.name) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User(" +
            "name=" + name +
            ", password=" + password +
            ")";
    }
}
