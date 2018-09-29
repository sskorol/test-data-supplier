package io.github.sskorol.entities;

import io.github.sskorol.data.FieldName;
import io.github.sskorol.data.Source;

@Source(path = "http://samplecsvs.s3.amazonaws.com/SacramentocrimeJanuary2006.csv")
public class CrimeRecord {

    private final String address;
    @FieldName("crimedescr")
    private final String description;

    public CrimeRecord(final String address, final String description) {
        this.address = address;
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var that = (CrimeRecord) o;

        if (address != null ? !address.equals(that.address) : that.address != null) {
            return false;
        }
        return description != null ? description.equals(that.description) : that.description == null;
    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CrimeRecord(" +
            "address=" + address +
            ", description=" + description +
            ")";
    }
}