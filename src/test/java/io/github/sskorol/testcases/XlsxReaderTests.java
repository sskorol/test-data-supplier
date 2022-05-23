package io.github.sskorol.testcases;

import io.github.sskorol.data.XlsxCellMapper;
import io.github.sskorol.data.XlsxReader;
import io.github.sskorol.entities.DummyXlsxData;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.github.sskorol.data.XlsxReader.defaultConverters;
import static org.testng.Assert.assertThrows;

public class XlsxReaderTests {

    @Test
    public void shouldThrowExceptionOnMissingNoArgsConstructor() throws NoSuchFieldException {
        var mapper = new XlsxCellMapper<DummyXlsxData>(
            DummyXlsxData.class.getDeclaredField("domain"),
            Map.of("Email", 1),
            defaultConverters()
        );
        var reader = new XlsxReader<>(DummyXlsxData.class);
        assertThrows(IllegalStateException.class, () -> reader.initEntity(List.of(mapper)));
    }
}
