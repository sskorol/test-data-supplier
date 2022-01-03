package io.github.sskorol.entities;

import com.creditdatamw.zerocell.annotation.Column;
import com.creditdatamw.zerocell.annotation.RowNumber;
import io.github.sskorol.data.Sheet;
import io.github.sskorol.data.Source;
import lombok.Data;

import java.time.LocalDate;

@Source(path = "people.xlsx")
@Sheet(name = "uploads")
@Data
public class PersonWithSheet {
    @RowNumber
    private int row;

    @Column(name = "FIRST_NAME", index = 1)
    private String firstName;

    @Column(name = "LAST_NAME", index = 3)
    private String lastName;

    @Column(name = "DATE_OF_BIRTH", index = 4)
    private LocalDate dateOfBirth;
}
