package com.autofocus.pms.hq.domain.common.carmaker.dto;

import lombok.Getter;

public class CarMakerResDTO {

    @Getter
    public static class CarMakerSearch {
        private Integer carMakerIdx;
        private String carMakerNm;

        public CarMakerSearch(Integer carMakerIdx, String carMakerNm) {
            this.carMakerIdx = carMakerIdx;
            this.carMakerNm = carMakerNm;
        }
    }
}