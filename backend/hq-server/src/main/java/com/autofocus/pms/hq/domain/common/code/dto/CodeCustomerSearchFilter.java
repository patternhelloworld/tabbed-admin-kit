package com.autofocus.pms.hq.domain.common.code.dto;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeCustomerSearchFilter {
    private String codeNm;
    private YNCode categoryYn;
    private String categoryNm;
}
