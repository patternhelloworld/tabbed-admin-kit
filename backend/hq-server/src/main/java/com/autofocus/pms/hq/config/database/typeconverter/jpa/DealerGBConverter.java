package com.autofocus.pms.hq.config.database.typeconverter.jpa;

import com.autofocus.pms.hq.config.database.typeconverter.DealerGb;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DealerGBConverter extends AbstractBaseEnumConverter<DealerGb, String> {

    @Override
    protected DealerGb[] getValueList() {
        return DealerGb.values();
    }
}