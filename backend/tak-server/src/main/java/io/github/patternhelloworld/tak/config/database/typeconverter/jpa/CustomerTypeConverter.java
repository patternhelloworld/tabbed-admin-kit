package io.github.patternhelloworld.tak.config.database.typeconverter.jpa;


import io.github.patternhelloworld.tak.config.database.typeconverter.CustomerType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CustomerTypeConverter extends AbstractBaseEnumConverter<CustomerType, Integer> {

    @Override
    protected CustomerType[] getValueList() {
        return CustomerType.values();
    }
}
