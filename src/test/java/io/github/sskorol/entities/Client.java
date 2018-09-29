package io.github.sskorol.entities;

import io.github.sskorol.data.Source;

@Source(path = "client.json")
public class Client {

    private final String firstName;
    private final String lastName;

    public Client(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var client = (Client) o;

        if (firstName != null ? !firstName.equals(client.firstName) : client.firstName != null) {
            return false;
        }
        return lastName != null ? lastName.equals(client.lastName) : client.lastName == null;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Client(" +
            "firstName=" + firstName +
            ", lastName=" + lastName +
            ")";
    }
}
