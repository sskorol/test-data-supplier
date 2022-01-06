package io.github.sskorol.entities;

import com.creditdatamw.zerocell.annotation.Column;
import com.creditdatamw.zerocell.converter.BooleanConverter;
import com.creditdatamw.zerocell.converter.IntegerConverter;
import io.github.sskorol.converters.PhoneNumberProcessor;
import io.github.sskorol.converters.StringToListConverter;
import io.github.sskorol.data.Sheet;
import io.github.sskorol.data.Source;
import lombok.Data;

import java.util.List;

@Data
@Source(path = "testdata.xlsx")
@Sheet(name = "Sheet1")
public class TestData {

    @Column(name = "TC", index=0 )
    private String testCase;

    @Column(name = "browser", index=1)
    private String browser;

    @Column(name = "firstname", index=2 )
    private String firstName;

    @Column(name = "isFTE", index=3, converterClass = BooleanConverter.class )
    private boolean isFTE;

    @Column(name="age",index = 4, converterClass = IntegerConverter.class)
    private int age;

    @Column(name="phonenumber",index = 5, converterClass = PhoneNumberProcessor.class )
    private String phoneNumber;

    @Column(name="list",index = 6, converterClass = StringToListConverter.class )
    private List<String> list;

}
