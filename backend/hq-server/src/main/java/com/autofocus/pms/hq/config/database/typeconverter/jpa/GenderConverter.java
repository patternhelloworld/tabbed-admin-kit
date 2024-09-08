package com.autofocus.pms.hq.config.database.typeconverter.jpa;

import com.autofocus.pms.hq.config.database.typeconverter.Gender;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GenderConverter extends AbstractBaseEnumConverter<Gender, String> {

    @Override
    protected Gender[] getValueList() {
        return Gender.values();
    }
}