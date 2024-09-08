package com.autofocus.pms.hq.domain.common.approvalline.dto;

import com.autofocus.pms.hq.config.database.typeconverter.LineGb;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprovalLineSearchFilter {
    private LineGb lineGb;
}
