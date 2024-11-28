package io.github.patternhelloworld.tak.config.database.typeconverter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LineGb implements BaseEnumCode<String> {
    A("계약"),
    C("해약"),
    D("시승");

    private final String value;
}