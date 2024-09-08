package com.autofocus.pms.hq.config.database.typeconverter.jpa;

import com.autofocus.pms.hq.config.database.typeconverter.PurchasePlan;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PurchasePlanConverter extends AbstractBaseEnumConverter<PurchasePlan, String> {

    @Override
    protected PurchasePlan[] getValueList() {
        return PurchasePlan.values();
    }
}