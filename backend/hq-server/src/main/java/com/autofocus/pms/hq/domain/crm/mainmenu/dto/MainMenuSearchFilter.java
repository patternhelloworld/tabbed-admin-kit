package com.autofocus.pms.hq.domain.crm.mainmenu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MainMenuSearchFilter {

    private String mainMenuNm;
    private String globalField;
}
