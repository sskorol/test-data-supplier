package io.github.sskorol.entities;

public class Token {

    private String secure;

    public Token() {
    }

    public Token(final String secure) {
        this.secure = secure;
    }

    public String getSecure() {
        return secure;
    }

    public void setSecure(final String secure) {
        this.secure = secure;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var token = (Token) o;

        return secure != null ? secure.equals(token.secure) : token.secure == null;
    }

    @Override
    public int hashCode() {
        return secure != null ? secure.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Token(" +
            "secure=" + secure +
            ")";
    }
}
