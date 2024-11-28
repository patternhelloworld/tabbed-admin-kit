package io.github.patternhelloworld.tak.config.database.typeconverter.jpa;

import io.github.patternhelloworld.tak.config.database.typeconverter.DealerStockUseType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DealerStockUseTypeConverter extends AbstractBaseEnumConverter<DealerStockUseType, Integer> {

    @Override
    protected DealerStockUseType[] getValueList() {
        return DealerStockUseType.values();
    }
}