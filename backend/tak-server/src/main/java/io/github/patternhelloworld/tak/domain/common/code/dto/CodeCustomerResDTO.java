package io.github.patternhelloworld.tak.domain.common.code.dto;

import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.code.entity.CodeCustomer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class CodeCustomerResDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class One {
        private Integer codeCustomerIdx;
        private String categoryCd;
        private String categoryNm;
        private YNCode categoryYn;
        private Integer parent;
        private Integer codeIdx2;
        private String nm;
        private Integer sort;

        private Integer dealerCd;

        private String regUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;
        private String modUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modDt;

        @QueryProjection
        public One(Integer codeCustomerIdx, String categoryCd, String categoryNm, YNCode categoryYn, Integer parent, Integer codeIdx2, String nm, Integer sort, Integer dealerCd, String regUserid, LocalDateTime regDt, String modUserid, LocalDateTime modDt) {
            this.codeCustomerIdx = codeCustomerIdx;
            this.categoryCd = categoryCd;
            this.categoryNm = categoryNm;
            this.categoryYn = categoryYn;
            this.parent = parent;
            this.codeIdx2 = codeIdx2;
            this.nm = nm;
            this.sort = sort;
            this.dealerCd = dealerCd;
            this.regUserid = regUserid;
            this.regDt = regDt;
            this.modUserid = modUserid;
            this.modDt = modDt;
        }
    }

    @Getter
    public static class Idx {
        private Integer codeCustomerIdx;
        public Idx(Integer codeCustomerIdx) {
            this.codeCustomerIdx = codeCustomerIdx;
        }
        public Idx(CodeCustomer codeCustomer) {
            this.codeCustomerIdx = codeCustomer.getCodeCustomerIdx();
        }
    }
}
