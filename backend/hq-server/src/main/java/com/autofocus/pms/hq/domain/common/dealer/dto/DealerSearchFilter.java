package com.autofocus.pms.hq.domain.common.dealer.dto;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DealerSearchFilter {
    private String dealerNm;
    private YNCode isMain;
    private String shortNm;
}
