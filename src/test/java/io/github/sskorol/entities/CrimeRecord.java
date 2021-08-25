package io.github.sskorol.entities;

import io.github.sskorol.data.FieldName;
import io.github.sskorol.data.Source;
import lombok.Data;

@Source(path = "https://raw.githubusercontent.com/JuliaData/CSV.jl/main/test/testfiles/SacramentocrimeJanuary2006.csv")
@Data
public class CrimeRecord {

    private final String address;
    @FieldName("crimedescr")
    private final String description;
}