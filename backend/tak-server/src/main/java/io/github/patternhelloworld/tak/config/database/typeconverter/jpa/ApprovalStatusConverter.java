package io.github.patternhelloworld.tak.config.database.typeconverter.jpa;

import io.github.patternhelloworld.tak.config.database.typeconverter.ApprovalStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ApprovalStatusConverter extends AbstractBaseEnumConverter<ApprovalStatus, Integer> {

    @Override
    protected ApprovalStatus[] getValueList() {
        return ApprovalStatus.values();
    }
}