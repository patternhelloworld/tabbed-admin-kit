package com.autofocus.pms.hq.config.database.typeconverter.jpa;


import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class YNCodeConverter extends AbstractBaseEnumConverter<YNCode, String> {

    @Override
    protected YNCode[] getValueList() {
        return YNCode.values();
    }
}