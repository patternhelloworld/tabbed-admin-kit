package com.autofocus.pms.hq.domain.common.vin.dto;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.carmodeldetail.entity.CarModelDetail;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import com.autofocus.pms.hq.domain.common.vin.entity.Vin;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class VinCommonDTO {

    @Getter
    @AllArgsConstructor
    public static class Updated {
        private Integer updated;
    }

    @Getter
    @AllArgsConstructor
    public static class VinIdx {
        private Long vinIdx;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class One {

        private Long vinIdx;

        // 검색창

        // 1. 년식
        private Integer carModelDetailYear;
        // 2. 제조사
        private Integer carMakerIdx;
        private String carMakerNm;

        // 3. 위 년식, 제조사 변경에 따라 달라지는 "모델명"
        private Integer carModelIdx;
        private String carModelCode;
        private String carModelName;
        private String carModelSvcCode;
        private String carModelSvcName;

        // 4. 위 모델 변경에 따라 달라지는 "세부 모델"
        private Integer carModelDetailIdx;
        private String carModelDetailName;
        private String carModelDetailCode;
        private String carModelDetailMotorType;
        private String carModelDetailCarName;

        // 차대번호
        private String vinNumber;


        private String regUserid;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;

        private String regIp;
        private String modUserid;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modDt;

        private String modIp;
        private String delUserid;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime delDt;

        private String delIp;

        private YNCode delYn;
        @JsonGetter("delYn")
        public String getDelYnValue() {
            return delYn != null ? delYn.getValue() : null;
        }

        @QueryProjection
        public One(Long vinIdx, Integer carModelDetailYear, Integer carMakerIdx, String carMakerNm, Integer carModelIdx, String carModelCode, String carModelName, String carModelSvcCode, String carModelSvcName, Integer carModelDetailIdx, String carModelDetailName, String carModelDetailCode, String carModelDetailMotorType, String carModelDetailCarName, String vinNumber, String regUserid, LocalDateTime regDt, String regIp, String modUserid, LocalDateTime modDt, String modIp, String delUserid, LocalDateTime delDt, String delIp, YNCode delYn) {
            this.vinIdx = vinIdx;
            this.carModelDetailYear = carModelDetailYear;
            this.carMakerIdx = carMakerIdx;
            this.carMakerNm = carMakerNm;
            this.carModelIdx = carModelIdx;
            this.carModelCode = carModelCode;
            this.carModelName = carModelName;
            this.carModelSvcCode = carModelSvcCode;
            this.carModelSvcName = carModelSvcName;
            this.carModelDetailIdx = carModelDetailIdx;
            this.carModelDetailName = carModelDetailName;
            this.carModelDetailCode = carModelDetailCode;
            this.carModelDetailMotorType = carModelDetailMotorType;
            this.carModelDetailCarName = carModelDetailCarName;
            this.vinNumber = vinNumber;
            this.regUserid = regUserid;
            this.regDt = regDt;
            this.regIp = regIp;
            this.modUserid = modUserid;
            this.modDt = modDt;
            this.modIp = modIp;
            this.delUserid = delUserid;
            this.delDt = delDt;
            this.delIp = delIp;
            this.delYn = delYn;
        }

        public Vin toEntity(CarModelDetail carModelDetail, User regUser) {
            return Vin.builder()
                    .carModelDetail(carModelDetail)
                    .vinNumber(this.vinNumber)
                    .regUser(regUser)
                    .regDt(LocalDateTime.now())
                    .regIp(regIp)
                    .modUserid(modUserid)
                    .modDt(modDt)
                    .modIp(modIp)
                    .delUserid(delUserid)
                    .delDt(delDt)
                    .delIp(delIp)
                    .delYn(delYn != null && delYn == YNCode.Y ? YNCode.Y : YNCode.N)
                    .build();
        }
    }
}
