package com.autofocus.pms.hq.domain.crm.submenu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubMenuSearchFilter {

    private String subMenuNm;
    private String globalField;
    private Integer subMenuIdx;
}

