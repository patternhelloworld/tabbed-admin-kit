package io.github.patternhelloworld.tak.domain.common.customer.dto;

import io.github.patternhelloworld.tak.config.database.typeconverter.*;
import io.github.patternhelloworld.tak.config.database.typeconverter.*;
import io.github.patternhelloworld.tak.domain.common.code.entity.CodeCustomer;
import io.github.patternhelloworld.tak.domain.common.customer.entity.Customer;
import io.github.patternhelloworld.tak.domain.common.customergroup.entity.CustomerGroup;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerCommonDTO {

    @Getter
    @AllArgsConstructor
    public static class Updated {
        private Integer updated;
    }

    @Getter
    @AllArgsConstructor
    public static class CustomerIdx {
        private Integer customerIdx;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class One {
        private Integer customerIdx;
        private String customerName;

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
        public String getPurchasePlan () {
            return purchasePlan != null ? purchasePlan.getValue() : null;
        }

        private String ownerName;
        private String lastNameEn;
        private String firstNameEn;
        private LocalDate birthDate;

        private Nationality nationality;
        @JsonGetter("nationality")
        public String getNationality () {
            return nationality != null ? nationality.getValue() : null;
        }

        private CustomerPurchaseType purchaseType;
        @JsonGetter("purchaseType")
        public Integer getPurchaseType() {
            return purchaseType != null ? purchaseType.getValue() : null;
        }

        private Gender gender;
        @JsonGetter("gender")
        public String getGender() {
            return gender != null ? gender.getValue() : null;
        }


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
        private String addrSi;
        private String addrGugun;
        private String addrBname;
        private String specialNotes;
        private String companyName;
        private Integer codeGeneralPositionIdx;
        private Integer codeGeneralJobIdx;

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

        private Integer codeGeneralContactMethodIdx;
        private Integer codeGeneralPurchaseDecisionFactorIdx;
        private Integer codeGeneralInterestFieldIdx;
        private String companyAddress;

        private YNCode personalDataConsent;
        @JsonGetter("personalDataConsent")
        public String getPersonalDataConsent() {
            return personalDataConsent != null ? personalDataConsent.getValue() : null;
        }

        private LocalDate personalDataConsentDate;

        private String chiefNm;
        private String bizNo;
        private String corporationNo;
        private String uptae;
        private String upjong;

        private Long userManagerIdx;
        private String userManagerName;

        private String regUserid;
        private String regUserName;

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
        @JsonGetter("delYn")
        public String getDelYnValue() {
            return delYn != null ? delYn.getValue() : null;
        }

        @QueryProjection
        public One(Integer customerIdx, String customerName, CustomerType customerType, CustomerInfo customerInfo, PurchasePlan purchasePlan, String ownerName, String lastNameEn, String firstNameEn, LocalDate birthDate, Nationality nationality, CustomerPurchaseType purchaseType, Gender gender, String email, String tel, String hp, String fax, String otherContact, Integer customerGroupIdx, CustomerGrade customerGrade, String zipcode, String addr1, String addr2, String addrSi, String addrGugun, String addrBname, String specialNotes, String companyName, Integer codeGeneralPositionIdx, Integer codeGeneralJobIdx, YNCode smsSubscription, YNCode emailSubscription, YNCode postalMailSubscription, Integer codeGeneralContactMethodIdx, Integer codeGeneralPurchaseDecisionFactorIdx, Integer codeGeneralInterestFieldIdx, String companyAddress, YNCode personalDataConsent, LocalDate personalDataConsentDate, String chiefNm, String bizNo, String corporationNo, String uptae, String upjong, Long userManagerIdx, String userManagerName, String regUserid, LocalDateTime regDt, String regIp, String modUserid, LocalDateTime modDt, String modIp, String delUserid, LocalDateTime delDt, String delIp, YNCode delYn) {
            this.customerIdx = customerIdx;
            this.customerName = customerName;
            this.customerType = customerType;
            this.customerInfo = customerInfo;
            this.purchasePlan = purchasePlan;
            this.ownerName = ownerName;
            this.lastNameEn = lastNameEn;
            this.firstNameEn = firstNameEn;
            this.birthDate = birthDate;
            this.nationality = nationality;
            this.purchaseType = purchaseType;
            this.gender = gender;
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
            this.addrSi = addrSi;
            this.addrGugun = addrGugun;
            this.addrBname = addrBname;
            this.specialNotes = specialNotes;
            this.companyName = companyName;
            this.codeGeneralPositionIdx = codeGeneralPositionIdx;
            this.codeGeneralJobIdx = codeGeneralJobIdx;
            this.smsSubscription = smsSubscription;
            this.emailSubscription = emailSubscription;
            this.postalMailSubscription = postalMailSubscription;
            this.codeGeneralContactMethodIdx = codeGeneralContactMethodIdx;
            this.codeGeneralPurchaseDecisionFactorIdx = codeGeneralPurchaseDecisionFactorIdx;
            this.codeGeneralInterestFieldIdx = codeGeneralInterestFieldIdx;
            this.companyAddress = companyAddress;
            this.personalDataConsent = personalDataConsent;
            this.personalDataConsentDate = personalDataConsentDate;
            this.chiefNm = chiefNm;
            this.bizNo = bizNo;
            this.corporationNo = corporationNo;
            this.uptae = uptae;
            this.upjong = upjong;
            this.userManagerIdx = userManagerIdx;
            this.userManagerName = userManagerName;
            this.regUserid = regUserid;
            this.regDt = regDt;
            this.regIp = regIp;
            this.modUserid = modUserid;
            this.modDt = modDt;
            this.modIp = modIp;
            this.delUserid = delUserid;
            this.delDt = delDt;
            this.delIp = delIp;
            this.delYn = delYn;
        }


        public Customer toEntity(CustomerGroup customerGroup, CodeCustomer positionCustomer, CodeCustomer jobCustomer,
                                 CodeCustomer contactMethodCustomer, CodeCustomer purchaseDecisionFactorCustomer, CodeCustomer interestFieldCustomer,
                                 User userManager, User regUser) {
            Customer customer = Customer.builder()
                    .customerIdx(this.customerIdx)
                    .customerName(this.customerName)
                    .customerType(this.customerType)
                    .customerInfo(this.customerInfo)
                    .purchasePlan(this.purchasePlan)
                    .ownerName(this.ownerName)
                    .lastNameEn(this.lastNameEn)
                    .firstNameEn(this.firstNameEn)
                    .birthDate(this.birthDate)
                    .nationality(this.nationality)
                    .purchaseType(this.purchaseType)
                    .gender(this.gender)
                    .email(this.email)
                    .tel(this.tel)
                    .hp(this.hp)
                    .fax(this.fax)
                    .otherContact(this.otherContact)
                    .customerGroup(customerGroup)  // Assuming there's a relationship with CustomerGroup entity
                    .customerGrade(this.customerGrade)
                    .zipcode(this.zipcode)
                    .addr1(this.addr1)
                    .addr2(this.addr2)
                    .addrSi(this.addrSi)
                    .addrGugun(this.addrGugun)
                    .addrBname(this.addrBname)
                    .specialNotes(this.specialNotes)
                    .companyName(this.companyName)
                    .codeGeneralPosition(positionCustomer)
                    .codeGeneralJob(jobCustomer)
                    .smsSubscription(this.smsSubscription)
                    .emailSubscription(this.emailSubscription)
                    .postalMailSubscription(this.postalMailSubscription)
                    .codeGeneralContactMethod(contactMethodCustomer)
                    .codeGeneralPurchaseDecisionFactor(purchaseDecisionFactorCustomer)
                    .codeGeneralInterestField(interestFieldCustomer)
                    .companyAddress(this.companyAddress)
                    .personalDataConsent(this.personalDataConsent)
                    .personalDataConsentDate(this.personalDataConsentDate)
                    .userManager(userManager)
                    .chiefNm(this.chiefNm)
                    .bizNo(this.bizNo)
                    .corporationNo(this.corporationNo)
                    .uptae(this.uptae)
                    .upjong(this.upjong)
                    .regUser(regUser)
                    .regDt(LocalDateTime.now())
                    .regIp(this.regIp)
                    .modUserid(this.modUserid)
                    .modDt(this.modDt)
                    .modIp(this.modIp)
                    .delUserid(this.delUserid)
                    .delDt(this.delDt)
                    .delIp(this.delIp)
                    .delYn(this.delYn != null && this.delYn == YNCode.Y ? YNCode.Y : YNCode.N)
                    .build();

            customer.setExpectedPurchaseDt();

            return customer;
        }

    }
}
