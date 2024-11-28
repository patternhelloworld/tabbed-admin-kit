package io.github.patternhelloworld.tak.domain.common.privacyagree.dto;

import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.privacyagree.entity.PrivacyAgree;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

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
