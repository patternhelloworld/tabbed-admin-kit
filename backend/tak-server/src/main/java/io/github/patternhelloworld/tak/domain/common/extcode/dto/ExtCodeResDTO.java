package io.github.patternhelloworld.tak.domain.common.extcode.dto;

import io.github.patternhelloworld.tak.domain.common.extcode.entity.ExtCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class ExtCodeResDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class One {
        private Integer ext_color_code_idx;
        private String code;
        private String description;
        private String regUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;

        private String modUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modDt;

        @QueryProjection
        public One(Integer ext_color_code_idx, String code, String description, String regUserid, LocalDateTime regDt, String modUserid, LocalDateTime modDt) {
            this.ext_color_code_idx = ext_color_code_idx;
            this.code = code;
            this.description = description;
            this.regUserid = regUserid;
            this.regDt = regDt;
            this.modUserid = modUserid;
            this.modDt = modDt;
        }

    }
    @Getter
    public static class Idx{
        private Integer ext_color_code_idx;
        public Idx(Integer ext_color_code_idx) { this.ext_color_code_idx = ext_color_code_idx; }
        public Idx(ExtCode extCode)
        {
            this.ext_color_code_idx = extCode.getExtColorCodeIdx();
        }
    }

}
