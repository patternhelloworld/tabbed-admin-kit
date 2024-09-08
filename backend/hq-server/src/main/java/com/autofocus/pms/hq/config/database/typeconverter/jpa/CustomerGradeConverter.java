package com.autofocus.pms.hq.config.database.typeconverter.jpa;

import com.autofocus.pms.hq.config.database.typeconverter.CustomerGrade;
import com.autofocus.pms.hq.config.database.typeconverter.PurchasePlan;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CustomerGradeConverter extends AbstractBaseEnumConverter<CustomerGrade, String> {

    @Override
    protected CustomerGrade[] getValueList() {
        return CustomerGrade.values();
    }
}