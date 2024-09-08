package com.autofocus.pms.hq.domain.common.privacyagree.dto;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.customergroup.entity.CustomerGroup;
import com.autofocus.pms.hq.domain.common.privacyagree.entity.PrivacyAgree;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class PrivacyAgreeResDTO {
    @Getter
    public static class One {
        private Integer privacyAgreeIdx;
        private YNCode isAgree;
        private String fname;
        private String sname;

        @QueryProjection
        public One(Integer privacyAgreeIdx, YNCode isAgree, String fname, String sname) {
            this.privacyAgreeIdx = privacyAgreeIdx;
            this.isAgree = isAgree;
            this.fname = fname;
            this.sname = sname;
        }
    }

    @Getter
    public static class Idx {
        private Integer privacyAgreeIdx;
        public Idx(Integer privacyAgreeIdx) {
            this.privacyAgreeIdx = privacyAgreeIdx;
        }
        public Idx(PrivacyAgree privacyAgree) {
            this.privacyAgreeIdx = privacyAgree.getPrivacyAgreeIdx();
        }
    }
}
