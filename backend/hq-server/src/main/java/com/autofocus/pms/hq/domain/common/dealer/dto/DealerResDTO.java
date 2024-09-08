package com.autofocus.pms.hq.domain.common.dealer.dto;

import com.autofocus.pms.hq.config.database.typeconverter.DealerGb;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.dealer.entity.Dealer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

public class DealerResDTO {
    @Getter
    public static class One {
        private Integer dealerCd;
        private DealerGb dealerGb;
        private String dealerNm;
        private String shortNm;
        private YNCode isMain;
        private String hphone;
        private String tel;
        private String fax;
        private String chiefNm;
        private String email;
        private String uptae;
        private String upjong;
        private String zipcode;
        private String addr1;
        private String addr2;
        private String pdiRequestCount;

        private String regUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;
        private String regIp;

        private String modUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modDt;
        private String modIp;

        @QueryProjection
        public One(Integer dealerCd, DealerGb dealerGb, String dealerNm, String shortNm, YNCode isMain, String hphone, String tel, String fax, String chiefNm, String email, String uptae, String upjong, String zipcode, String addr1, String addr2, String pdiRequestCount, String regUserid, LocalDateTime regDt, String regIp, String modUserid, LocalDateTime modDt, String modIp) {
            this.dealerCd = dealerCd;
            this.dealerGb = dealerGb;
            this.dealerNm = dealerNm;
            this.shortNm = shortNm;
            this.isMain = isMain;
            this.hphone = hphone;
            this.tel = tel;
            this.fax = fax;
            this.chiefNm = chiefNm;
            this.email = email;
            this.uptae = uptae;
            this.upjong = upjong;
            this.zipcode = zipcode;
            this.addr1 = addr1;
            this.addr2 = addr2;
            this.pdiRequestCount = pdiRequestCount;
            this.regUserid = regUserid;
            this.regDt = regDt;
            this.regIp = regIp;
            this.modUserid = modUserid;
            this.modDt = modDt;
            this.modIp = modIp;
        }
    }


    @Getter
    public static class CdNm {
        private Integer dealerCd;
        private String dealerNm;
        @QueryProjection
        public CdNm(Integer dealerCd, String dealerNm) {
            this.dealerCd = dealerCd;
            this.dealerNm = dealerNm;
        }
    }

    @Getter
    public static class DealerCd {
        private Integer dealerCd;
        public DealerCd(Dealer dealer) { this.dealerCd = dealer.getDealerCd(); }
    }


}
