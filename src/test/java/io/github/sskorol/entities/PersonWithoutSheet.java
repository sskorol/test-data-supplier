package io.github.sskorol.entities;

import io.github.sskorol.data.Column;
import io.github.sskorol.data.Source;
import lombok.Data;

import java.time.LocalDate;

@Source(path = "people.xlsx")
@Data
public class PersonWithoutSheet {
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;
}
