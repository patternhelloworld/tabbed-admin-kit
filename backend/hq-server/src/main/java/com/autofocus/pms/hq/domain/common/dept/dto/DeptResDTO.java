package com.autofocus.pms.hq.domain.common.dept.dto;

import com.autofocus.pms.common.enums.PrivacyNumberType;
import com.autofocus.pms.common.util.CustomUtils;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.dept.entity.Dept;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

public class DeptResDTO {

    @Getter
    public static class IdxNm {
        private Integer deptIdx;
        private String deptNm;
        @QueryProjection
        public IdxNm(Integer deptIdx, String deptNm) {
            this.deptIdx = deptIdx;
            this.deptNm = deptNm;
        }
    }

    @Getter
    public static class One {
        private Integer deptIdx;
        private Integer dealerCd;
        private String dealerNm;
        private Integer depth;
        private String deptNm;
        private Integer parentCd;

        private Integer menuNum;
        private YNCode viewYn;
        private Integer deptSort;
        private String selfNm;
        private String selfBizNo;
        private String selfCorporationNo;
        private String selfChiefNm;
        private String selfUptae;
        private String selfUpjong;
        private String selfRegId;
        private String selfManager;
        private String selfHphone;
        private String selfFax;
        private String selfZipcode;
        private String selfAddr1;
        private String selfAddr2;
        private String selfEmail;
        private String selfImgFname;
        private String selfImgSname;

        private String regUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;
        private String regIp;

        private String modUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modDt;
        private String modIp;

        @QueryProjection
        public One(Integer deptIdx, Integer dealerCd, String dealerNm, String deptNm, Integer parentCd, Integer menuNum, YNCode viewYn,
                   Integer deptSort, String selfNm, String selfBizNo, String selfCorporationNo, String selfChiefNm, String selfUptae,
                   String selfUpjong,
                   String selfRegId, String selfManager, String selfHphone, String selfFax, String selfZipcode, String selfAddr1,
                   String selfAddr2,
                   String selfEmail, String selfImgFname, String selfImgSname, String regUserid, LocalDateTime regDt, String regIp,
                   String modUserid,
                   LocalDateTime modDt, String modIp) {
            this.deptIdx = deptIdx;
            this.dealerCd = dealerCd;
            this.dealerNm = dealerNm;

            this.deptNm = deptNm;
            this.parentCd = parentCd;

            this.menuNum = menuNum;
            this.viewYn = viewYn;
            this.deptSort = deptSort;
            this.selfNm = selfNm;

            String decryptBizNo = CustomUtils.decryptECB(selfBizNo);
            this.selfBizNo = CustomUtils.makeNumberPattern(decryptBizNo, PrivacyNumberType.사업자번호.getValue());

            this.selfCorporationNo = selfCorporationNo;
            this.selfChiefNm = selfChiefNm;
            this.selfUptae = selfUptae;
            this.selfUpjong = selfUpjong;

            String decryptRegId = CustomUtils.decryptECB(selfRegId);
            this.selfRegId = CustomUtils.makeNumberPattern(decryptRegId, PrivacyNumberType.사업자번호.getValue());

            this.selfManager = selfManager;
            this.selfHphone = selfHphone;
            this.selfFax = selfFax;
            this.selfZipcode = selfZipcode;
            this.selfAddr1 = selfAddr1;
            this.selfAddr2 = selfAddr2;
            this.selfEmail = selfEmail;
            this.selfImgFname = selfImgFname;
            this.selfImgSname = selfImgSname;
            this.regUserid = regUserid;
            this.regDt = regDt;
            this.regIp = regIp;
            this.modUserid = modUserid;
            this.modDt = modDt;
            this.modIp = modIp;
        }
    }

    @Getter
    public static class DeptIdx {
        private Integer deptIdx;
        public DeptIdx(Dept dept) { this.deptIdx = dept.getDeptIdx(); }
    }
}
