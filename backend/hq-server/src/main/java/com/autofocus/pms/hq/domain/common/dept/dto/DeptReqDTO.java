package com.autofocus.pms.hq.domain.common.dept.dto;

import com.autofocus.pms.common.util.CustomUtils;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.dealer.entity.Dealer;
import com.autofocus.pms.hq.domain.common.dept.entity.Dept;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class DeptReqDTO {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateUpdateOne {
        private Integer deptIdx;
        private Dealer dealer;
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
        private String selfAddrSi;
        private String selfAddrGugun;
        private String selfAddrBname;
        private String selfEmail;
        private String selfImgFname;
        private String selfImgSname;
        private String regUserId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;
        private String regIp;
        private String modUserId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modDt;
        private String modIp;
        private String delUserId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime delDt;
        private String delIp;
        private YNCode delYn;

        public Dept toEntity(Dealer dealer) {

            String encryptSelfBizNo = CustomUtils.encryptECB(this.selfBizNo);
            String encryptSelfRegId = CustomUtils.encryptECB(this.selfRegId);

            Dept.DeptBuilder builder = Dept.builder()
                    .dealer(dealer)
                    .deptNm(this.deptNm)
                    .parentCd(this.parentCd)
                    .menuNum(this.menuNum)
                    .viewYn(YNCode.Y)
                    .deptSort(this.deptSort)
                    .selfNm(this.selfNm)
                    .selfBizNo(encryptSelfBizNo)
                    .selfCorporationNo(this.selfCorporationNo)
                    .selfChiefNm(this.selfChiefNm)
                    .selfUptae(this.selfUptae)
                    .selfUpjong(this.selfUpjong)
                    .selfRegId(encryptSelfRegId)
                    .selfManager(this.selfManager)
                    .selfHphone(this.selfHphone)
                    .selfFax(this.selfFax)
                    .selfZipcode(this.selfZipcode)
                    .selfAddr1(this.selfAddr1)
                    .selfAddr2(this.selfAddr2)
                    .selfAddrSi(this.selfAddrSi)
                    .selfAddrGugun(this.selfAddrGugun)
                    .selfAddrBname(this.selfAddrBname)
                    .selfEmail(this.selfEmail)
                    .selfImgFname(this.selfImgFname)
                    .selfImgSname(this.selfImgSname)
                    .regUserId(this.regUserId)
                    .regDt(LocalDateTime.now())
                    .regIp(this.regIp)
                    .modUserId(this.modUserId)
                    .modDt(this.modDt)
                    .modIp(this.modIp)
                    .delUserId(this.delUserId)
                    .delDt(this.delDt)
                    .delIp(this.delIp)
                    .delYn(YNCode.N);
            return builder.build();
        }
    }
}
