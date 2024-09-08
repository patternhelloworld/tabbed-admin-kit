package com.autofocus.pms.hq.config.database.typeconverter.jpa;

import com.autofocus.pms.hq.config.database.typeconverter.Nationality;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NationalityConverter extends AbstractBaseEnumConverter<Nationality, String> {

    @Override
    protected Nationality[] getValueList() {
        return Nationality.values();
    }
}