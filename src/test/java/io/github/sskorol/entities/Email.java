package io.github.sskorol.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Email {

    private List<String> recipients;
}
