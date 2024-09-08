package com.autofocus.pms.hq.domain.common.customergroup.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerGroupSearchFilter {
    private String groupNm;
}
