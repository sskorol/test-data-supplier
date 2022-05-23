package io.github.sskorol.converters;

public class EmailConverter extends DefaultConverter<String> {

    @Override
    public String convert(final String value) {
        var data = value.split("@");
        return data.length == 2 ? data[1] : value;
    }
}
