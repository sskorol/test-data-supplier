package io.github.sskorol.entities;

import io.github.sskorol.converters.PhoneNumberProcessor;
import io.github.sskorol.converters.StringToListIConverter;
import io.github.sskorol.data.Column;
import io.github.sskorol.data.Sheet;
import io.github.sskorol.data.Sheets;
import io.github.sskorol.data.Source;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Source(path = "random.xlsx")
@Sheets(value = {
    @Sheet(name = "Sheet1"),
    @Sheet(name = "Sheet2")
})
public class XlsxData {

    @Column(name = "TC")
    private String testCase;

    @Column(name = "Browser")
    private String browser;

    @Column(name = "Date")
    private LocalDate date;

    @Column(name = "Next Check", format = "yyyy-MM-dd hh:mm:ss a")
    private LocalDateTime nextCheck;

    @Column(name = "Price")
    private double price;

    @Column(name = "Should Run")
    private boolean shouldRun;

    @Column(name = "Age")
    private int age;

    @Column(name = "Phone Number", converter = PhoneNumberProcessor.class)
    private String phoneNumber;

    @Column(name = "Tech Stack", converter = StringToListIConverter.class)
    private List<String> techStack;
}
