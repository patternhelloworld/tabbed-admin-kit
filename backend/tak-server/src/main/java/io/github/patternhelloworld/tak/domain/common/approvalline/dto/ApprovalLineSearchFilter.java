package io.github.patternhelloworld.tak.domain.common.approvalline.dto;

import io.github.patternhelloworld.tak.config.database.typeconverter.LineGb;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprovalLineSearchFilter {
    private LineGb lineGb;
}
