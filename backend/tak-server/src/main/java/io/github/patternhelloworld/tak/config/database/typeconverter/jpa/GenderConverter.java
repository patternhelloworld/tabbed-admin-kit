package io.github.patternhelloworld.tak.config.database.typeconverter.jpa;

import io.github.patternhelloworld.tak.config.database.typeconverter.Gender;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GenderConverter extends AbstractBaseEnumConverter<Gender, String> {

    @Override
    protected Gender[] getValueList() {
        return Gender.values();
    }
}