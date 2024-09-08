package com.autofocus.pms.hq.config.database.typeconverter.jpa;

import com.autofocus.pms.hq.config.database.typeconverter.DealerStockUseType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DealerStockUseTypeConverter extends AbstractBaseEnumConverter<DealerStockUseType, Integer> {

    @Override
    protected DealerStockUseType[] getValueList() {
        return DealerStockUseType.values();
    }
}