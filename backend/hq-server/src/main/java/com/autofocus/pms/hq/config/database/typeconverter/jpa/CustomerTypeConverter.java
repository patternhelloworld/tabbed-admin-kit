package com.autofocus.pms.hq.config.database.typeconverter.jpa;


import com.autofocus.pms.hq.config.database.typeconverter.CustomerType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CustomerTypeConverter extends AbstractBaseEnumConverter<CustomerType, Integer> {

    @Override
    protected CustomerType[] getValueList() {
        return CustomerType.values();
    }
}
