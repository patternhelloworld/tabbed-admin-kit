package com.autofocus.pms.hq.config.database.typeconverter.jpa;

import com.autofocus.pms.hq.config.database.typeconverter.CustomerInfo;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CustomerInfoConverter extends AbstractBaseEnumConverter<CustomerInfo, Integer> {

    @Override
    protected CustomerInfo[] getValueList() {
        return CustomerInfo.values();
    }
}