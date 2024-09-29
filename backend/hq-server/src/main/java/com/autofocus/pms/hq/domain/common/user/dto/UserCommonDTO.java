package com.autofocus.pms.hq.domain.common.user.dto;

import com.autofocus.pms.hq.config.database.typeconverter.ManagementDepartment;
import com.autofocus.pms.hq.config.database.typeconverter.ViewPermission;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.dept.entity.Dept;
import com.autofocus.pms.hq.domain.common.user.entity.Password;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import com.autofocus.pms.hq.domain.common.user.utils.PasswordUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class UserCommonDTO {

    @Getter
    @AllArgsConstructor
    public static class UserIdx {
        private Long userIdx;
    }

    @Getter
    public static class One {

        private Long userIdx;
        private String name;
        private String userId;

        @QueryProjection
        public One(Long userIdx, String name, String userId) {
            this.userIdx = userIdx;
            this.name = name;
            this.userId = userId;
        }
    }

    /*
    *
    *   사용자 메뉴 권한 조회 및 수정
    *   사용자 정보 조회 및 수정
    *    : otp 민감 정보 제외
    * */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OneWithDeptDealer {

        private Long userIdx;
        private String userId;

        // @JsonIgnore 는 Serialization 에서 무시한다는 의미.
        @JsonIgnore
        private String password;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime passwordChangedAt;
        private Integer passwordFailedCount;


        private String name;
        private String position;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate joiningDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate birthDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate resignationDate;

        private String phoneNumber;

        private String zipcode;
        private String addr1;
        private String addr2;
        private String addrSi;
        private String addrGugun;
        private String addrBname;

        private ManagementDepartment managementDepartment;
        @JsonGetter("managementDepartment")
        public String getManagementDepartment() {
            return managementDepartment != null ? managementDepartment.getValue() : null;
        }

        private ViewPermission viewPermission;
        @JsonGetter("viewPermission")
        public String getViewPermission() {
            return viewPermission != null ? viewPermission.getValue() : null;
        }


        private YNCode dmsWorkerYn;
        @JsonGetter("dmsWorkerYn")
        public String getDmsWorkerYnValue() {
            return dmsWorkerYn != null ? dmsWorkerYn.getValue() : null;
        }


        private String deptNm;
        private Integer deptIdx;

        private String dealerNm;
        private Integer dealerCd;


        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;
        private String regUserId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modDt;
        private String modUserId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime delDt;
        private String delUserId;
        private YNCode delYn;
        @JsonGetter("delYn")
        public String getDelYnValue() {
            return delYn != null ? delYn.getValue() : null;
        }

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate outDt;
        private YNCode outYn;
        @JsonGetter("outYn")
        public String getOutYnValue() {
            return outYn != null ? outYn.getValue() : null;
        }
        // MyBatis 에서 사용 (select 문의 순서와 일치해야 한다)
        // JPA 사용 시 : @QueryProjection 필요
        public User toEntity(Dept dept, String creator){
            return User.builder()
                    .userIdx(this.userIdx)
                    .userId(this.userId)
                    .password(Password.builder().value(PasswordUtils.encrypt("da329")).failedCount(0).changedDate(LocalDateTime.now()).build())
                    .name(this.name)
                    .userId(this.userId)
                    .position(this.position)
                    .joiningDate(this.joiningDate)
                    .birthDate(this.birthDate)
                    .resignationDate(this.resignationDate)
                    .phoneNumber(this.phoneNumber)
                    .zipcode(this.zipcode)
                    .addr1(this.addr1)
                    .addr2(this.addr2)
                    .addrSi(this.addrSi)
                    .addrGugun(this.addrGugun)
                    .addrBname(this.addrBname)
                    .managementDepartment(this.managementDepartment)
                    .viewPermission(this.viewPermission)
                    .dept(dept)
                    .regDt(LocalDateTime.now())
                    .regUserId(creator)
                    .modDt(this.modDt)
                    .modUserId(this.modUserId)
                    .delDt(this.delDt)
                    .delUserId(this.delUserId)
                    .delYn(this.delDt != null ? YNCode.Y : YNCode.N)
                    .outDt(this.outDt)
                    .outYn(this.outDt != null ? YNCode.Y : YNCode.N)
                    .build();
        }

    }


    /*
     *
     *   사용자 자신 세션 조회
     *    : 프론트 메뉴 권한을 선택적으로....
     * */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class OneWithDeptDealerMenus {

        private Long userIdx;
        private String userId;

        private String password;
        @JsonProperty("password")
        public String getPasswordForJson() {
            return "";
        }

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime passwordChangedAt;
        private Integer passwordFailedCount;

        private String name;
        private String position;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate joiningDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate birthDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate resignationDate;

        private String phoneNumber;

        private String zipcode;
        private String addr1;
        private String addr2;

        private ManagementDepartment managementDepartment;
        @JsonGetter("managementDepartment")
        public String getManagementDepartment() {
            return managementDepartment != null ? managementDepartment.getValue() : null;
        }

        private ViewPermission viewPermission;
        @JsonGetter("viewPermission")
        public String getViewPermission() {
            return viewPermission != null ? viewPermission.getValue() : null;
        }


        private YNCode dmsWorkerYn;
        @JsonGetter("dmsWorkerYn")
        public String getDmsWorkerYnValue() {
            return dmsWorkerYn != null ? dmsWorkerYn.getValue() : null;
        }


        private String deptNm;
        private Integer deptIdx;

        private String dealerNm;
        private Integer dealerCd;


        private List<Permission> permissions;
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Permission {
            private YNCode ynLst;
            private YNCode ynInt;
            private YNCode ynMod;
            private YNCode ynDel;
            private YNCode ynXls;
            private String subMenuNm;
            private String subMenuPath;
            private String subMenuKey;
            private String mainMenuNm;
            private String mainMenuPath;
            private String mainMenuKey;
        }


        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;
        private String regUserId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modDt;
        private String modUserId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime delDt;
        private String delUserId;

        // MyBatis 에서 사용 (select 문의 순서와 일치해야 한다)
        // JPA 사용 시 : @QueryProjection 필요
        public OneWithDeptDealerMenus(Long userIdx, String userId, String password, LocalDateTime passwordChangedAt, Integer passwordFailedCount, String name, String position, LocalDate joiningDate, LocalDate birthDate, LocalDate resignationDate, String phoneNumber, String zipcode, String addr1, String addr2, ManagementDepartment managementDepartment, ViewPermission viewPermission, YNCode dmsWorkerYn, String deptNm, Integer deptIdx, String dealerNm, Integer dealerCd, List<Permission> permissions, LocalDateTime regDt, String regUserId, LocalDateTime modDt, String modUserId, LocalDateTime delDt, String delUserId) {
            this.userIdx = userIdx;
            this.userId = userId;
            this.password = password;
            this.passwordChangedAt = passwordChangedAt;
            this.passwordFailedCount = passwordFailedCount;
            this.name = name;
            this.position = position;
            this.joiningDate = joiningDate;
            this.birthDate = birthDate;
            this.resignationDate = resignationDate;
            this.phoneNumber = phoneNumber;
            this.zipcode = zipcode;
            this.addr1 = addr1;
            this.addr2 = addr2;
            this.managementDepartment = managementDepartment;
            this.viewPermission = viewPermission;
            this.dmsWorkerYn = dmsWorkerYn;
            this.deptNm = deptNm;
            this.deptIdx = deptIdx;
            this.dealerNm = dealerNm;
            this.dealerCd = dealerCd;
            this.permissions = permissions;
            this.regDt = regDt;
            this.regUserId = regUserId;
            this.modDt = modDt;
            this.modUserId = modUserId;
            this.delDt = delDt;
            this.delUserId = delUserId;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OneWithDept {
        private Long userIdx;
        private String name;
        private String position;
        private String deptNm;
        @QueryProjection
        public OneWithDept(Long userIdx, String name, String position, String deptNm) {
            this.userIdx = userIdx;
            this.name = name;
            this.position = position;
            this.deptNm = deptNm;
        }
    }
}
