package io.github.patternhelloworld.tak.config.database.typeconverter.jpa;

import io.github.patternhelloworld.tak.config.database.typeconverter.CustomerInfo;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CustomerInfoConverter extends AbstractBaseEnumConverter<CustomerInfo, Integer> {

    @Override
    protected CustomerInfo[] getValueList() {
        return CustomerInfo.values();
    }
}