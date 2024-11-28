package io.github.patternhelloworld.tak.domain.common.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSearchFilter {

    private String userId;

    private String name;
    private String dealerNm;
    private String deptNm;

    private String globalField;

    private Integer dealerCd;

    private String position;

}
