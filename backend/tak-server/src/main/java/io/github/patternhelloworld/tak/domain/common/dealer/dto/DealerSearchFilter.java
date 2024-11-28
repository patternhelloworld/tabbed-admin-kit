package io.github.patternhelloworld.tak.domain.common.dealer.dto;

import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
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
