package com.autofocus.pms.hq.domain.common.customergroup.dto;

import com.autofocus.pms.hq.domain.common.customergroup.entity.CustomerGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class CustomerGroupResDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class One {
        private Integer customerGroupIdx;
        private Integer dealerCd;
        private String userid;
        private String groupNm;
        private String regUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;

        private String modUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modDt;

        @QueryProjection
        public One(Integer customerGroupIdx, Integer dealerCd, String userid, String groupNm, String regUserid, LocalDateTime regDt, String modUserid, LocalDateTime modDt) {
            this.customerGroupIdx = customerGroupIdx;
            this.dealerCd = dealerCd;
            this.userid = userid;
            this.groupNm = groupNm;
            this.regUserid = regUserid;
            this.regDt = regDt;
            this.modUserid = modUserid;
            this.modDt = modDt;
        }
    }

    @Getter
    public static class Idx {
        private Integer customerGroupIdx;
        public Idx(Integer customerGroupIdx) {
            this.customerGroupIdx = customerGroupIdx;
        }
        public Idx(CustomerGroup customerGroup) {
            this.customerGroupIdx = customerGroup.getCustomerGroupIdx();
        }
    }
}
