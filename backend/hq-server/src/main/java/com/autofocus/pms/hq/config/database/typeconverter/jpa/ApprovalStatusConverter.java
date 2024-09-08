package com.autofocus.pms.hq.config.database.typeconverter.jpa;

import com.autofocus.pms.hq.config.database.typeconverter.ApprovalStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ApprovalStatusConverter extends AbstractBaseEnumConverter<ApprovalStatus, Integer> {

    @Override
    protected ApprovalStatus[] getValueList() {
        return ApprovalStatus.values();
    }
}