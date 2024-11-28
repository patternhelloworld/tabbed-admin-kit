package io.github.patternhelloworld.tak.domain.common.dept.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeptSearchFilter {
    private String deptNm;
    private Integer dealerCd;

    private Boolean onlySecondDepth;
}
