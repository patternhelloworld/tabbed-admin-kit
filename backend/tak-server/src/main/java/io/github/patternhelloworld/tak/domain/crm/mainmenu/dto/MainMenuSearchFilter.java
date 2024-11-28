package io.github.patternhelloworld.tak.domain.crm.mainmenu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MainMenuSearchFilter {

    private String mainMenuNm;
    private String globalField;
}
