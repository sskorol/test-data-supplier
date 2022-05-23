package io.github.sskorol.entities;

import io.github.sskorol.converters.EmailConverter;
import io.github.sskorol.data.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Map;

public class DummyXlsxData {

    @Getter
    @Column(name = "Email", converter = EmailConverter.class)
    private String domain;

    @Column(name = "JSON")
    private Map<Integer, String> json;

    private String name;

    public DummyXlsxData(String name) {
        this.name = name;
    }
}
