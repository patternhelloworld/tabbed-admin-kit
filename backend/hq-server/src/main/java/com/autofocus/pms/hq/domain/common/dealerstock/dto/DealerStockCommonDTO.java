package com.autofocus.pms.hq.domain.common.dealerstock.dto;

import com.autofocus.pms.hq.config.database.typeconverter.DealerStockUseType;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.dealerstock.entity.DealerStock;
import com.autofocus.pms.hq.domain.common.dept.entity.Dept;
import com.autofocus.pms.hq.domain.common.stock.entity.Stock;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DealerStockCommonDTO {

    @Getter
    @AllArgsConstructor
    public static class Updated {
        private Integer updated;
    }

    @Getter
    @AllArgsConstructor
    public static class DealerStockIdx {
        private Long dealerStockIdx;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Create {

        private Integer deptIdx;
        private Long vinIdx;

        // 사용중
        private DealerStockUseType useType;
        @JsonGetter("useType")
        public Integer getUseType() {
            return useType != null ? useType.getValue() : null;
        }

        private LocalDate importDate;

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


        public DealerStock toEntity(Stock stock, Dept dept, User regUser) {
            return DealerStock.builder()
                    .stock(stock)
                    .dept(dept)
                    .importDate(this.importDate)
                    .useType(this.useType)
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

    @Getter
    @Setter
    @NoArgsConstructor
    public static class One {

        private Long dealerStockIdx;

        // 검색창

        // 0. 사용중
        private DealerStockUseType useType;
        @JsonGetter("useType")
        public Integer getUseType() {
            return useType != null ? useType.getValue() : null;
        }

        // 1. 전시장
        private Integer deptIdx;
        private String deptNm;

        // 2. 년식
        private Integer carModelDetailYear;

        // 3. 제조사
        private Integer carMakerIdx;
        private String carMakerNm;

        // 4. 위 제조사 변경에 따라 달라지는 "모델명"
        private Integer carModelIdx;
        private String carModelCode;
        private String carModelName;
        private String carModelSvcCode;
        private String carModelSvcName;


        // 5. 위 모델 변경에 따라 달라지는 "세부 모델"
        private Integer carModelDetailIdx;
        private String carModelDetailName;
        private String carModelDetailCode;
        private String carModelDetailMotorType;
        private String carModelDetailCarName;


        // stock 관련 데이터는 현재 없으나 추가될 경우 여기에
        private Long stockIdx;

        // 차대번호
        private Long vinIdx;
        private String vinNumber;


        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate importDate;

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
        public One(Long dealerStockIdx, DealerStockUseType useType, Integer deptIdx, String deptNm, Integer carModelDetailYear, Integer carMakerIdx, String carMakerNm, Integer carModelIdx, String carModelCode, String carModelName, String carSvcCode, String carModelSvcName,
                   Integer carModelDetailIdx, String carModelDetailName, String carModelDetailCode, String carModelDetailMotorType, String carModelDetailCarName,
                   Long stockIdx, Long vinIdx,
                   String vinNumber, LocalDate importDate, String regUserid, LocalDateTime regDt, String regIp, String modUserid, LocalDateTime modDt, String modIp, String delUserid, LocalDateTime delDt, String delIp, YNCode delYn) {

            this.dealerStockIdx = dealerStockIdx;
            this.useType = useType;
            this.deptIdx = deptIdx;
            this.deptNm = deptNm;
            this.carModelDetailYear = carModelDetailYear;
            this.carMakerIdx = carMakerIdx;
            this.carMakerNm = carMakerNm;

            this.carModelIdx = carModelIdx;
            this.carModelCode = carModelCode;
            this.carModelName = carModelName;
            this.carModelSvcCode = carSvcCode;
            this.carModelSvcName = carModelSvcName;

            this.carModelDetailIdx = carModelDetailIdx;
            this.carModelDetailName = carModelDetailName;
            this.carModelDetailCode = carModelDetailCode;
            this.carModelDetailMotorType = carModelDetailMotorType;
            this.carModelDetailCarName = carModelDetailCarName;

            this.stockIdx = stockIdx;
            this.vinIdx = vinIdx;
            this.vinNumber = vinNumber;
            this.importDate = importDate;
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

    }
}
