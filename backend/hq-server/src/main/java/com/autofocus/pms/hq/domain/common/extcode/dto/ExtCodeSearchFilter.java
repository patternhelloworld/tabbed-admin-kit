package com.autofocus.pms.hq.domain.common.extcode.dto;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtCodeSearchFilter {
    private String code;
    private String description;
    private String globalField;
}
