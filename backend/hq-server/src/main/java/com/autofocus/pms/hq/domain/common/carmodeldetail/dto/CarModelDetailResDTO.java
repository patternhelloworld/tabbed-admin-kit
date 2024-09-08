package com.autofocus.pms.hq.domain.common.carmodeldetail.dto;

import lombok.Getter;

public class CarModelDetailResDTO {

    @Getter
    public static class CarModelDetailSearch {
        private Integer carModelDetailIdx;
        private String name;
        private String code;
        private String motorType;
        private String carName;

        public CarModelDetailSearch(Integer carModelDetailIdx, String name, String code, String motorType, String carName) {
            this.carModelDetailIdx = carModelDetailIdx;
            this.name = name;
            this.code = code;
            this.motorType = motorType;
            this.carName = carName;
        }
    }

}
