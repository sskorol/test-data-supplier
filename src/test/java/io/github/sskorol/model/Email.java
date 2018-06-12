package io.github.sskorol.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Email {

    private List<String> recipients;
}
