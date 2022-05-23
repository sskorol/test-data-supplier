package io.github.sskorol.testcases;

import io.github.sskorol.converters.*;
import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.XlsxCellMapper;
import io.github.sskorol.entities.DummyXlsxData;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import one.util.streamex.StreamEx;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static io.github.sskorol.converters.LocalDateTimeConverter.DEFAULT_FORMAT;
import static io.github.sskorol.data.XlsxReader.defaultConverters;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertThrows;

public class ConvertersTests {

    @DataSupplier(propagateTestFailure = true, flatMap = true)
    public List<Tuple> getDoubles() {
        return List.of(
            Tuple.of("0.00", 0.00d),
            Tuple.of(".001", 0.001),
            Tuple.of("-1", -1.00d),
            Tuple.of("0.0000001", 0.0000001),
            Tuple.of(String.valueOf(Double.MIN_VALUE), Double.MIN_VALUE),
            Tuple.of(String.valueOf(Double.MAX_VALUE), Double.MAX_VALUE)
        );
    }

    @DataSupplier(propagateTestFailure = true, flatMap = true)
    public List<Tuple> getIntegers() {
        return List.of(
            Tuple.of("0", 0),
            Tuple.of("1", 1),
            Tuple.of("-1", -1),
            Tuple.of(String.valueOf(Integer.MIN_VALUE), Integer.MIN_VALUE),
            Tuple.of(String.valueOf(Integer.MAX_VALUE), Integer.MAX_VALUE)
        );
    }

    @DataSupplier(propagateTestFailure = true, flatMap = true)
    public List<Tuple> getBooleans() {
        return List.of(
            Tuple.of("true", true),
            Tuple.of("false", false),
            Tuple.of("TRUE", true),
            Tuple.of("FALSE", false),
            Tuple.of("True", true),
            Tuple.of("fALSE", false)
        );
    }

    @DataSupplier(propagateTestFailure = true, flatMap = true)
    public StreamEx<Tuple> getDates() {
        return StreamEx
            .of("2024-02-29", "2022-01-01", "2022-12-31")
            .map(date -> Tuple.of(date, LocalDate.parse(date)));
    }

    @DataSupplier(propagateTestFailure = true, flatMap = true)
    public StreamEx<Tuple> getDatesWithCustomFormat() {
        var format = "dd-MM-yyyy";
        return StreamEx
            .of("29-02-2024", "01-01-2022", "31-12-2022")
            .map(date -> Tuple.of(date, LocalDate.parse(date, ofPattern(format)), format));
    }

    @DataSupplier(propagateTestFailure = true, flatMap = true)
    public StreamEx<Tuple> getDateTimes() {
        return StreamEx
            .of("2024-02-29 00:00:00", "2022-01-01 23:59:59", "2022-12-31 12:01:01")
            .map(dateTime -> Tuple.of(dateTime, LocalDateTime.parse(dateTime, ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }

    @DataSupplier(propagateTestFailure = true, flatMap = true)
    public StreamEx<Tuple3<String, LocalDateTime, String>> getDateTimesWithCustomFormat() {
        var format = "dd-MM-yyyy hh:mm:ss a";
        var dates = List.of("29-02-2024 00:00:00 AM", "01-01-2022 11:59:59 PM", "31-12-2022 12:01:01 PM");
        var extraDate = "2024-02-29 00:00:00";
        var extractExpected = Tuple.of(extraDate, LocalDateTime.parse(extraDate, ofPattern(DEFAULT_FORMAT)));
        return StreamEx
            .of(dates)
            .map(dateTime -> Tuple.of(dateTime, LocalDateTime.parse(dateTime, ofPattern(format)), format))
            .append(StreamEx.of(extractExpected.append(null), extractExpected.append("")));
    }

    @DataSupplier(propagateTestFailure = true, flatMap = true)
    public StreamEx<Tuple> getErrors() {
        return StreamEx.of(
            Tuple.of(IllegalStateException.class, "json", 2),
            Tuple.of(IllegalArgumentException.class, "name", 3)
        );
    }

    @Test(dataProvider = "getDoubles")
    public void shouldConvertToDouble(String value, Double expected) {
        var actual = new DoubleConverter().convert(value);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldNotConvertToDouble() {
        assertThrows(IllegalArgumentException.class, () -> new DoubleConverter().convert("not a number"));
    }

    @Test(dataProvider = "getIntegers")
    public void shouldConvertToInteger(String value, Integer expected) {
        var actual = new IntegerConverter().convert(value);
        assertThat(actual).isEqualTo(expected);
    }

    @Test(dataProvider = "getBooleans")
    public void shouldConvertToBoolean(String value, Boolean expected) {
        var actual = new BooleanConverter().convert(value);
        assertThat(actual).isEqualTo(expected);
    }

    @Test(dataProvider = "getDates")
    public void shouldConvertToLocalDate(String value, LocalDate expected) {
        var actual = new LocalDateConverter().convert(value);
        assertThat(actual).isEqualTo(expected);
    }

    @Test(dataProvider = "getDatesWithCustomFormat")
    public void shouldConvertToLocalDateWithCustomFormat(String value, LocalDate expected, String format) {
        var actual = new LocalDateConverter().convert(value, format);
        assertThat(actual).isEqualTo(expected);
    }

    @Test(dataProvider = "getDateTimes")
    public void shouldConvertToLocalDateTime(String value, LocalDateTime expected) {
        var actual = new LocalDateTimeConverter().convert(value);
        assertThat(actual).isEqualTo(expected);
    }

    @Test(dataProvider = "getDateTimesWithCustomFormat")
    public void shouldConvertToLocalDateTimeWithCustomFormat(String value, LocalDateTime expected, String format) {
        var actual = new LocalDateTimeConverter().convert(value, format);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldReturnTheSameString() {
        var target = "TesT 1";
        var actual = new StringConverter().convert(target);
        assertThat(actual).isEqualTo(target);
    }

    @Test
    public void shouldApplyCustomConverter() throws NoSuchFieldException {
        var expectedDomain = "gmail.com";
        var entity = new DummyXlsxData("name");
        var cellMapper = new XlsxCellMapper<>(
            entity
                .getClass()
                .getDeclaredField("domain"),
            Map.of("Email", 1),
            defaultConverters()
        );

        var row = mock(Row.class);
        var cell = mock(Cell.class);
        var text = new HSSFRichTextString(format("test@%s", expectedDomain));

        when(cell.getCellType()).thenReturn(CellType.STRING);
        when(cell.getRichStringCellValue()).thenReturn(text);
        when(row.getCell(1, RETURN_BLANK_AS_NULL)).thenReturn(cell);

        cellMapper
            .parse(row)
            .assignValue(entity);
        assertThat(entity.getDomain()).isEqualTo(expectedDomain);
    }

    @Test(dataProvider = "getErrors")
    public void shouldThrowConverterError(Class<Throwable> ex, String fieldName, int columnIndex) {
        assertThrows(ex, () -> new XlsxCellMapper<>(
            DummyXlsxData.class.getDeclaredField(fieldName),
            Map.of(fieldName.toUpperCase(), columnIndex),
            defaultConverters()
        ));
    }
}
