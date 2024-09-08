package com.autofocus.pms.hq.domain.common.dealer.dto;

import com.autofocus.pms.hq.config.database.typeconverter.DealerGb;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.dealer.entity.Dealer;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DealerReqDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateUpdateOne {
        private Integer dealerCd;
        private DealerGb dealerGb;
        private YNCode isMain;
        private String dealerNm;
        private String shortNm;
        private String tel;
        private String hphone;
        private String fax;
        private String chiefNm;
        private String email;
        private String uptae;
        private String upjong;
        private String zipcode;
        private String addr1;
        private String addr2;
        private String addrSi;
        private String addrGugun;
        private String addrBname;
        private String pdiRequestCount;
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

        public Dealer toEntity() {
            Dealer.DealerBuilder builder = Dealer.builder()
                    .dealerGb(this.dealerGb)
                    .isMain(this.isMain)
                    .dealerNm(this.dealerNm)
                    .shortNm(this.shortNm)
                    .tel(this.tel)
                    .hphone(this.hphone)
                    .fax(this.fax)
                    .chiefNm(this.chiefNm)
                    .email(this.email)
                    .uptae(this.uptae)
                    .upjong(this.upjong)
                    .zipcode(this.zipcode)
                    .addr1(this.addr1)
                    .addr2(this.addr2)
                    .addrSi(this.addrSi)
                    .addrGugun(this.addrGugun)
                    .addrBname(this.addrBname)
                    .pdiRequestCount(this.pdiRequestCount)
                    .regUserid(this.regUserid)
                    .regDt(LocalDateTime.now())
                    .regIp(this.regIp)
                    .modUserid(this.modUserid)
                    .modDt(this.modDt)
                    .modIp(this.modIp)
                    .delUserid(this.delUserid)
                    .delDt(this.delDt)
                    .delIp(this.delIp)
                    .delYn(this.delYn);
            return builder.build();
        }

    }


}
