package io.github.patternhelloworld.tak.config.database.typeconverter.jpa;


import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class YNCodeConverter extends AbstractBaseEnumConverter<YNCode, String> {

    @Override
    protected YNCode[] getValueList() {
        return YNCode.values();
    }
}