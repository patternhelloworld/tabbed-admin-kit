package io.github.patternhelloworld.tak.domain.common.carmodel.dto;

import lombok.Getter;

public class CarModelResDTO {

    @Getter
    public static class CarModelSearch {

        private Integer carModelIdx;
        private String modelCode;
        private String modelName;
        private String svcCode;
        private String svcName;
        private String modelSvcName;

        public CarModelSearch(Integer carModelIdx, String modelCode, String modelName, String svcCode, String svcName, String modelSvcName) {
            this.carModelIdx = carModelIdx;
            this.modelCode = modelCode;
            this.modelName = modelName;
            this.svcCode = svcCode;
            this.svcName = svcName;
            this.modelSvcName = modelSvcName;
        }
    }
}