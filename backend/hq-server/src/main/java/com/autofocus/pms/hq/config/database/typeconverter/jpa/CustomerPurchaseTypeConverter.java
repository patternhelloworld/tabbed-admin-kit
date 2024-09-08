package com.autofocus.pms.hq.config.database.typeconverter.jpa;


import com.autofocus.pms.hq.config.database.typeconverter.CustomerPurchaseType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CustomerPurchaseTypeConverter extends AbstractBaseEnumConverter<CustomerPurchaseType, Integer> {

    @Override
    protected CustomerPurchaseType[] getValueList() {
        return CustomerPurchaseType.values();
    }
}