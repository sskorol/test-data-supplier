package io.github.sskorol.entities;

import com.creditdatamw.zerocell.annotation.Column;
import io.github.sskorol.converters.*;
import io.github.sskorol.data.Sheet;
import io.github.sskorol.data.Source;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@Source(path = "testdata.xlsx")
@Sheet(name = "Sheet1")
public class TestData {

    @Column(name = "TC", index=0 )
    private String testcase;

    @Column(name = "browser", index=1)
    private String browser;

    @Column(name = "firstname", index=2 )
    private String firstname;

    @Column(name = "isFTE", index=3, converterClass = StringToBooleanConverter.class )
    private boolean isFTE;

    @Column(name="age",index = 4, converterClass = StringToIntegerConverter.class)
    private int age;

    @Column(name="date",index = 5, converterClass = StringToLocalDateConverter.class)
    private LocalDate date;

    @Column(name="phonenumber",index = 6, converterClass = PhoneNumberProcessor.class )
    private String phonenumber;

    @Column(name="list",index = 7, converterClass = StringToListConverter.class )
    private List<String> list;


}
