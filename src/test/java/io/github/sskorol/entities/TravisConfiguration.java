package io.github.sskorol.entities;

import io.github.sskorol.data.Source;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Source(path = "https://raw.githubusercontent.com/sskorol/test-data-supplier/master/.travis.yml")
@Data
@NoArgsConstructor
public class TravisConfiguration {

    private String language;
    private boolean sudo;
    private boolean install;
    private Map<String, Addon> addons;
    private List<String> jdk;
    private List<String> script;
    private Map<String, List<String>> cache;
    private Map<String, Email> notifications;
}
