package io.github.patternhelloworld.tak.domain.common.relocatelog.dto;

import io.github.patternhelloworld.tak.config.database.typeconverter.*;
import io.github.patternhelloworld.tak.config.database.typeconverter.*;
import io.github.patternhelloworld.tak.domain.common.relocatelog.entity.RelocateLog;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
public class RelocateLogResDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class One {

        private Integer relocateLogIdx;
        private Integer customerIdx;
        private String customerName;
        private String fromUserManagerNm;
        private String toUserManagerNm;
        private String relocateRegUserid;
        
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime relocateRegDt; //이관일

        private Long userManagerIdx;
        private String userManagerName;

        
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

        private PurchasePlan purchasePlan;

        @JsonGetter("purchasePlan")
        public String getPurchasePlan() {
            return purchasePlan != null ? purchasePlan.getValue() : null;
        }
        private LocalDate birthDate;

        private String email;
        private String tel;
        private String hp;
        private String fax;
        private String otherContact;
        private Integer customerGroupIdx;

        private CustomerGrade customerGrade;
        @JsonGetter("customerGrade")
        public String getCustomerGrade() {
            return customerGrade != null ? customerGrade.getValue() : null;
        }

        private String zipcode;
        private String addr1;
        private String addr2;
        private YNCode smsSubscription;

        @JsonGetter("smsSubscription")
        public String getSmsSubscription() {
            return smsSubscription != null ? smsSubscription.getValue() : null;
        }

        private YNCode emailSubscription;

        @JsonGetter("emailSubscription")
        public String getEmailSubscription() {
            return emailSubscription != null ? emailSubscription.getValue() : null;
        }

        private YNCode postalMailSubscription;

        @JsonGetter("postalMailSubscription")
        public String getPostalMailSubscription() {
            return postalMailSubscription != null ? postalMailSubscription.getValue() : null;
        }

        private YNCode personalDataConsent;
        @JsonGetter("personalDataConsent")
        public String getPersonalDataConsent() {
            return personalDataConsent != null ? personalDataConsent.getValue() : null;
        }
        private LocalDate personalDataConsentDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;//고객등록일
        private String regIp;
        
        private YNCode delYn;
        @JsonGetter("delYn")
        public String getDelYnValue() {
            return delYn != null ? delYn.getValue() : null;
        }

        @QueryProjection
        public One(Integer relocateLogIdx, Integer customerIdx, String customerName, String fromUserManagerNm, String toUserManagerNm, String relocateRegUserid, LocalDateTime relocateRegDt, Long userManagerIdx, String userManagerName, CustomerType customerType, CustomerInfo customerInfo, PurchasePlan purchasePlan, LocalDate birthDate, String email, String tel, String hp, String fax, String otherContact, Integer customerGroupIdx, CustomerGrade customerGrade, String zipcode, String addr1, String addr2, YNCode smsSubscription, YNCode emailSubscription, YNCode postalMailSubscription, YNCode personalDataConsent, LocalDate personalDataConsentDate, LocalDateTime regDt, String regIp, YNCode delYn) {
            this.relocateLogIdx = relocateLogIdx;
            this.customerIdx = customerIdx;
            this.customerName = customerName;
            this.fromUserManagerNm = fromUserManagerNm;
            this.toUserManagerNm = toUserManagerNm;
            this.relocateRegUserid = relocateRegUserid;
            this.relocateRegDt = relocateRegDt;
            this.userManagerIdx = userManagerIdx;
            this.userManagerName = userManagerName;
            this.customerType = customerType;
            this.customerInfo = customerInfo;
            this.purchasePlan = purchasePlan;
            this.birthDate = birthDate;
            this.email = email;
            this.tel = tel;
            this.hp = hp;
            this.fax = fax;
            this.otherContact = otherContact;
            this.customerGroupIdx = customerGroupIdx;
            this.customerGrade = customerGrade;
            this.zipcode = zipcode;
            this.addr1 = addr1;
            this.addr2 = addr2;
            this.smsSubscription = smsSubscription;
            this.emailSubscription = emailSubscription;
            this.postalMailSubscription = postalMailSubscription;
            this.personalDataConsent = personalDataConsent;
            this.personalDataConsentDate = personalDataConsentDate;
            this.regDt = regDt;
            this.regIp = regIp;
            this.delYn = delYn;
        }
    }

    @Getter
    public static class Idx {
        private Integer relocateLogIdx;
        public Idx(Integer relocateLogIdx) {
            this.relocateLogIdx = relocateLogIdx;
        }
        public Idx(RelocateLog relocateLog) {
            this.relocateLogIdx = relocateLog.getRelocateLogIdx();
        }
    }
}
