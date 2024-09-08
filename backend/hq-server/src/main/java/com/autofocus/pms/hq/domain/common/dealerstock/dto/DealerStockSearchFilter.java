package com.autofocus.pms.hq.domain.common.dealerstock.dto;

import com.autofocus.pms.hq.config.database.typeconverter.DealerStockUseType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DealerStockSearchFilter {
    private Long dealerStockIdx;

    private DealerStockUseType useType;
    // 전시장 검색
    private Integer deptIdx;
    // 연식
    private Integer carModelDetailYear;
    // 제조사
    private Integer carMakerIdx;
    // 모델 선택
    private Integer carModelIdx;
    // 모델 디테일 선택
    private Integer carModelDetailIdx;
    // 차대 번호
    private String vinNumber;
}
