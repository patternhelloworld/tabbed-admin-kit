package io.github.patternhelloworld.tak.config.database.typeconverter.jpa;

import io.github.patternhelloworld.tak.config.database.typeconverter.CustomerGrade;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CustomerGradeConverter extends AbstractBaseEnumConverter<CustomerGrade, String> {

    @Override
    protected CustomerGrade[] getValueList() {
        return CustomerGrade.values();
    }
}