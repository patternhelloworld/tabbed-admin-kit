package com.autofocus.pms.hq.domain.common.testdrive.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestDriveSearchFilter {

    private Long testDriveIdx;

    // 전시장
    private Integer deptIdx;
    // 차대 번호
    private String vinNumber;
    // 차량 번호
    private String carNo;

}
