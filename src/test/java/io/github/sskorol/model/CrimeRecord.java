package io.github.sskorol.model;

import io.github.sskorol.csv.Source;
import io.github.sskorol.csv.FieldName;
import lombok.Data;

@Data
@Source(path = "http://samplecsvs.s3.amazonaws.com/SacramentocrimeJanuary2006.csv")
public class CrimeRecord {

    private final String address;
    @FieldName("crimedescr")
    private final String description;
}
