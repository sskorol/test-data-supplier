package io.github.sskorol.entities;

public class Addon {

    private String organization;
    private Token token;

    public Addon() {
    }

    public Addon(final String organization, final Token token) {
        this.organization = organization;
        this.token = token;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(final String organization) {
        this.organization = organization;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(final Token token) {
        this.token = token;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var addon = (Addon) o;

        if (organization != null ? !organization.equals(addon.organization) : addon.organization != null) {
            return false;
        }
        return token != null ? token.equals(addon.token) : addon.token == null;
    }

    @Override
    public int hashCode() {
        int result = organization != null ? organization.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Addon(" +
            "organization=" + organization +
            ", token=" + token +
            ")";
    }
}
