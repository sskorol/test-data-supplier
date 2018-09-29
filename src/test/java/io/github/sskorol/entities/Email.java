package io.github.sskorol.entities;

import java.util.List;

public class Email {

    private List<String> recipients;

    public Email() {
    }

    public Email(final List<String> recipients) {
        this.recipients = recipients;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(final List<String> recipients) {
        this.recipients = recipients;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var email = (Email) o;

        return recipients != null ? recipients.equals(email.recipients) : email.recipients == null;
    }

    @Override
    public int hashCode() {
        return recipients != null ? recipients.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Email(" +
            "recipients=" + recipients +
            ")";
    }
}
