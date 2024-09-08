package com.autofocus.pms.hq.domain.common.testdrive.dto;

import com.autofocus.pms.hq.config.database.typeconverter.ApprovalStatus;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.customer.entity.Customer;
import com.autofocus.pms.hq.domain.common.testdrive.entity.TestDrive;
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

public class TestDriveCommonDTO {

    @Getter
    @AllArgsConstructor
    public static class Updated {
        private Integer updated;
    }

    @Getter
    @AllArgsConstructor
    public static class TestDriveIdx {
        private Long testDriveIdx;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class One {

        private Long testDriveIdx;

        private Long vinIdx;
        private String vinNumber;

        private Integer deptIdx;
        private String deptNm;

        private Long userIdx;
        private String userName;

        private Integer customerIdx;
        private String customerName;

        private String carNo;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime startDate;
        private Integer startMile;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime endDate;
        private Integer endMile;
        private String testPlace;
        private String remainFuel;
        private String mileage;
        private Integer fuelFee;
        private Integer washFee;
        private String comment;


        private ApprovalStatus isApproved;
        // 만약 이게 없다면 Response DTO 로 쓸 때, ApprovalStatus 의 예를 들어 "PENDING" 이런 스트링이 리턴 됨.
        // 만약 이게 없다면 Request DTO 로 쓸 때, 값을 이 정수 값으로 받을 수 없음.
        @JsonGetter("isApproved")
        public Integer getCustomerType() {
            return isApproved != null ? isApproved.getValue() : null;
        }

        private YNCode isDrive;

        private String regUserid;
        private LocalDateTime regDt;

        private String modUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modDt;

        private String delUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime delDt;

        private YNCode delYn;
        @JsonGetter("delYn")
        public String getDelYnValue() {
            return delYn != null ? delYn.getValue() : null;
        }

        @QueryProjection
        public One(Long testDriveIdx, Long vinIdx, String vinNumber, Integer deptIdx, String deptNm, Long userIdx, String userName, Integer customerIdx, String customerName, String carNo, LocalDateTime startDate, Integer startMile, LocalDateTime endDate, Integer endMile, String testPlace, String remainFuel, String mileage, Integer fuelFee, Integer washFee, String comment,
                   ApprovalStatus isApproved,
                   YNCode isDrive, String regUserid, LocalDateTime regDt, String modUserid, LocalDateTime modDt, String delUserid, LocalDateTime delDt, YNCode delYn) {
            this.testDriveIdx = testDriveIdx;
            this.vinIdx = vinIdx;
            this.vinNumber = vinNumber;
            this.deptIdx = deptIdx;
            this.deptNm = deptNm;
            this.userIdx = userIdx;
            this.userName = userName;
            this.customerIdx = customerIdx;
            this.customerName = customerName;
            this.carNo = carNo;
            this.startDate = startDate;
            this.startMile = startMile;
            this.endDate = endDate;
            this.endMile = endMile;
            this.testPlace = testPlace;
            this.remainFuel = remainFuel;
            this.mileage = mileage;
            this.fuelFee = fuelFee;
            this.washFee = washFee;
            this.comment = comment;

            this.isApproved = isApproved;
            this.isDrive = isDrive;
            this.regUserid = regUserid;
            this.regDt = regDt;
            this.modUserid = modUserid;
            this.modDt = modDt;
            this.delUserid = delUserid;
            this.delDt = delDt;
            this.delYn = delYn;
        }


        public TestDrive toEntity(Vin vin, User user, Customer customer) {
            return TestDrive.builder()
                    .vin(vin)
                    .user(user)
                    .customer(customer)
                    .carNo(carNo)
                    .startDate(startDate)
                    .startMile(startMile)
                    .endDate(endDate)
                    .endMile(endMile)
                    .testPlace(testPlace)
                    .remainFuel(remainFuel)
                    .mileage(mileage)
                    .fuelFee(fuelFee)
                    .washFee(washFee)
                    .comment(comment)
                    .isApproved(isApproved)
                    .isDrive(isDrive)
                    .regUserid(regUserid)
                    .regDt(LocalDateTime.now())
                    .modUserid(modUserid)
                    .modDt(modDt)
                    .delUserid(delUserid)
                    .delDt(delDt)
                    .delYn(delYn != null && delYn == YNCode.Y ? YNCode.Y : YNCode.N)
                    .build();
        }
    }
}
