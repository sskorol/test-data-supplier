package io.github.sskorol.model;

import io.github.sskorol.data.FieldName;
import io.github.sskorol.data.Source;
import lombok.Data;

@Data
@Source(path = "http://samplecsvs.s3.amazonaws.com/SacramentocrimeJanuary2006.csv")
public class CrimeRecord {

    private final String address;
    @FieldName("crimedescr")
    private final String description;
}
