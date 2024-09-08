package com.autofocus.pms.hq.domain.common.groupassign.dto;

import com.autofocus.pms.hq.config.database.typeconverter.*;
import com.autofocus.pms.hq.domain.common.groupassign.entity.GroupAssign;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GroupAssignResDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class One {
        private Integer customerIdx;
        private String customerName;

        private CustomerType customerType;
        @JsonGetter("customerType")
        public Integer getCustomerType() {
            return customerType != null ? customerType.getValue() : null;
        }

        private CustomerInfo customerInfo;
        @JsonGetter("customerInfo")
        public Integer getCustomerInfo() {
            return customerInfo != null ? customerInfo.getValue() : null;
        }

        private LocalDate birthDate;

        private Gender gender;
        @JsonGetter("gender")
        public String getGender() {
            return gender != null ? gender.getValue() : null;
        }

        private String email;
        private String tel;
        private String hp;
        private String fax;

        private Long userManagerIdx;
        private String userManagerName;

        private String regUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;
        private String regIp;
        private String modUserid;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modDt;
        private String modIp;

        /*private YNCode delYn;
        @JsonGetter("delYn")
        public String getDelYnValue() {
            return delYn != null ? delYn.getValue() : null;
        }*/

        private Integer customerGroupIdx;
        private String groupNm;

        @QueryProjection
        public One(Integer customerIdx, String customerName, CustomerType customerType, CustomerInfo customerInfo, LocalDate birthDate, Gender gender, String email, String tel, String hp, String fax, Long userManagerIdx, String userManagerName, String regUserid, LocalDateTime regDt, String regIp, String modUserid, LocalDateTime modDt, String modIp, Integer customerGroupIdx, String groupNm) {
            this.customerIdx = customerIdx;
            this.customerName = customerName;
            this.customerType = customerType;
            this.customerInfo = customerInfo;
            this.birthDate = birthDate;
            this.gender = gender;
            this.email = email;
            this.tel = tel;
            this.hp = hp;
            this.fax = fax;
            this.userManagerIdx = userManagerIdx;
            this.userManagerName = userManagerName;
            this.regUserid = regUserid;
            this.regDt = regDt;
            this.regIp = regIp;
            this.modUserid = modUserid;
            this.modDt = modDt;
            this.modIp = modIp;
            this.customerGroupIdx = customerGroupIdx;
            this.groupNm = groupNm;
        }
    }

    @Getter
    public static class Idx {
        private Integer groupAssignIdx;
        public Idx(Integer groupAssignIdx) {
            this.groupAssignIdx = groupAssignIdx;
        }
        public Idx(GroupAssign groupAssign) {
            this.groupAssignIdx = groupAssign.getGroupAssignIdx();
        }
    }
}
