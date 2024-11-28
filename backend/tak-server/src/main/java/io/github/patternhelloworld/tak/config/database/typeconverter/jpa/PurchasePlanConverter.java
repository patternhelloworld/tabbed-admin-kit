package io.github.patternhelloworld.tak.config.database.typeconverter.jpa;

import io.github.patternhelloworld.tak.config.database.typeconverter.PurchasePlan;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PurchasePlanConverter extends AbstractBaseEnumConverter<PurchasePlan, String> {

    @Override
    protected PurchasePlan[] getValueList() {
        return PurchasePlan.values();
    }
}