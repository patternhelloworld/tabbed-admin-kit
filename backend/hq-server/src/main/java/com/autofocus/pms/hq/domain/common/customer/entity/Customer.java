package com.autofocus.pms.hq.domain.common.customer.entity;

import com.autofocus.pms.hq.config.database.typeconverter.*;
import com.autofocus.pms.hq.config.database.typeconverter.jpa.*;
import com.autofocus.pms.hq.domain.common.code.entity.CodeCustomer;
import com.autofocus.pms.hq.domain.common.customer.dto.CustomerCommonDTO;
import com.autofocus.pms.hq.domain.common.customergroup.entity.CustomerGroup;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_idx")
    private Integer customerIdx;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_type")
    @Convert(converter = CustomerTypeConverter.class)
    private CustomerType customerType;

    @Column(name = "customer_info")
    @Convert(converter = CustomerInfoConverter.class)
    private CustomerInfo customerInfo;

    @Column(name = "purchase_plan")
    @Convert(converter = PurchasePlanConverter.class)
    private PurchasePlan purchasePlan;

    @Column(name = "expected_purchase_dt")
    private LocalDateTime expectedPurchaseDt;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "last_name_en")
    private String lastNameEn;

    @Column(name = "first_name_en")
    private String firstNameEn;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "nationality")
    @Convert(converter = NationalityConverter.class)
    private Nationality nationality;

    @Column(name = "purchase_type")
    @Convert(converter = CustomerPurchaseTypeConverter.class)
    private CustomerPurchaseType purchaseType;

    @Column(name = "gender")
    @Convert(converter = GenderConverter.class)
    private Gender gender;

    @Column(name = "email")
    private String email;

    @Column(name = "tel")
    private String tel;

    @Column(name = "hp")
    private String hp;

    @Column(name = "fax")
    private String fax;

    @Column(name = "other_contact")
    private String otherContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_group_idx")
    private CustomerGroup customerGroup;

    @Column(name = "customer_grade")
    @Convert(converter = CustomerGradeConverter.class)
    private CustomerGrade customerGrade;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "addr1")
    private String addr1;

    @Column(name = "addr2")
    private String addr2;

    @Column(name = "addr_si")
    private String addrSi;

    @Column(name = "addr_gugun")
    private String addrGugun;

    @Column(name = "addr_bname")
    private String addrBname;

    @Column(name = "special_notes")
    private String specialNotes;

    @Column(name = "company_name")
    private String companyName;


    @Column(name = "sms_subscription")
    @Convert(converter = YNCodeConverter.class)
    private YNCode smsSubscription;

    @Column(name = "email_subscription")
    @Convert(converter = YNCodeConverter.class)
    private YNCode emailSubscription;

    @Column(name = "postal_mail_subscription")
    @Convert(converter = YNCodeConverter.class)
    private YNCode postalMailSubscription;


    @Column(name = "code_general_position_idx", insertable = false, updatable = false)
    private Integer codeGeneralPositionIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_general_position_idx", referencedColumnName = "code_customer_idx")
    private CodeCustomer codeGeneralPosition;

    @Column(name = "code_general_job_idx", insertable = false, updatable = false)
    private Integer codeGeneralJobIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_general_job_idx", referencedColumnName = "code_customer_idx")
    private CodeCustomer codeGeneralJob;

    @Column(name = "code_general_contact_method_idx", insertable = false, updatable = false)
    private Integer codeGeneralContactMethodIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_general_contact_method_idx")
    private CodeCustomer codeGeneralContactMethod;

    @Column(name = "code_general_purchase_decision_factor_idx", insertable = false, updatable = false)
    private Integer codeGeneralPurchaseDecisionFactorIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_general_purchase_decision_factor_idx", referencedColumnName = "code_customer_idx")
    private CodeCustomer codeGeneralPurchaseDecisionFactor;

    @Column(name = "code_general_interest_field_idx", insertable = false, updatable = false)
    private Integer codeGeneralInterestFieldIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_general_interest_field_idx", referencedColumnName = "code_customer_idx")
    private CodeCustomer codeGeneralInterestField;

    @Column(name = "company_address")
    private String companyAddress;

    @Column(name = "personal_data_consent")
    @Convert(converter = YNCodeConverter.class)
    private YNCode personalDataConsent;

    @Column(name = "personal_data_consent_date")
    private LocalDate personalDataConsentDate;

    @Column(name = "chief_nm")
    private String chiefNm;

    @Column(name = "biz_no")
    private String bizNo;

    @Column(name = "corporation_no")
    private String corporationNo;

    @Column(name = "uptae")
    private String uptae;

    @Column(name = "upjong")
    private String upjong;

    @Column(name = "user_manager_idx", insertable = false, updatable = false)
    private Long userManagerIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_manager_idx", referencedColumnName = "user_idx")
    private User userManager;

    @Column(name = "reg_userid", insertable = false, updatable = false)
    private String regUserid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reg_userid", referencedColumnName = "user_id")
    private User regUser;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "reg_ip")
    private String regIp;

    @Column(name = "mod_userid")
    private String modUserid;

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    @Column(name = "mod_ip")
    private String modIp;

    @Setter
    @Column(name = "del_userid")
    private String delUserid;

    @Setter
    @Column(name = "del_dt")
    private LocalDateTime delDt;

    @Setter
    @Column(name = "del_ip")
    private String delIp;

    @Setter
    @Convert(converter = YNCodeConverter.class)
    @Column(name = "del_yn")
    private YNCode delYn;


    public void setExpectedPurchaseDt() {
        if (this.regDt != null && this.purchasePlan != null) {
            switch (this.purchasePlan) {
                case IMMEDIATE :
                    this.expectedPurchaseDt = this.regDt;
                    break;
                case ONE_MONTH:
                    this.expectedPurchaseDt = this.regDt.plusMonths(1);
                    break;
                case THREE_MONTHS:
                    this.expectedPurchaseDt = this.regDt.plusMonths(3);
                    break;
                case SIX_MONTHS:
                    this.expectedPurchaseDt = this.regDt.plusMonths(6);
                    break;
                case TWELVE_MONTHS:
                    this.expectedPurchaseDt = this.regDt.plusMonths(12);
                    break;
                default:
                    this.expectedPurchaseDt = null;
            }
        }
    }

    public void updateCustomer(CustomerCommonDTO.One dto, CustomerGroup customerGroup, CodeCustomer positionCustomer, CodeCustomer jobCustomer,
                               CodeCustomer contactMethodCustomer, CodeCustomer purchaseDecisionFactorCustomer, CodeCustomer interestFieldCustomer, String modifier) {
        // 기본 정보 업데이트
        this.customerName = dto.getCustomerName();
        this.customerType = BaseEnumCode.valueOf(CustomerType.class, dto.getCustomerType());
        this.customerInfo = BaseEnumCode.valueOf(CustomerInfo.class, dto.getCustomerInfo());

        this.purchasePlan = BaseEnumCode.valueOf(PurchasePlan.class, dto.getPurchasePlan());
        setExpectedPurchaseDt();

        this.ownerName = dto.getOwnerName();
        this.lastNameEn = dto.getLastNameEn();
        this.firstNameEn = dto.getFirstNameEn();
        this.birthDate = dto.getBirthDate();
        this.nationality = BaseEnumCode.valueOf(Nationality.class, dto.getNationality());
        this.purchaseType = BaseEnumCode.valueOf(CustomerPurchaseType.class, dto.getPurchaseType());
        this.gender = BaseEnumCode.valueOf(Gender.class, dto.getGender());
        this.email = dto.getEmail();
        this.tel = dto.getTel();
        this.hp = dto.getHp();
        this.fax = dto.getFax();
        this.otherContact = dto.getOtherContact();
        this.chiefNm = dto.getChiefNm();
        this.bizNo = dto.getBizNo();
        this.corporationNo = dto.getCorporationNo();
        this.uptae = dto.getUptae();
        this.upjong = dto.getUpjong();

        // CustomerGroup 설정
        this.customerGroup = customerGroup;

        // 기타 정보 업데이트
        this.customerGrade = BaseEnumCode.valueOf(CustomerGrade.class, dto.getCustomerGrade());
        this.zipcode = dto.getZipcode();
        this.addr1 = dto.getAddr1();
        this.addr2 = dto.getAddr2();
        this.addrSi = dto.getAddrSi();
        this.addrGugun = dto.getAddrGugun();
        this.addrBname = dto.getAddrBname();
        this.specialNotes = dto.getSpecialNotes();
        this.companyName = dto.getCompanyName();

        // YNCode 필드 업데이트
        this.smsSubscription = BaseEnumCode.valueOf(YNCode.class, dto.getSmsSubscription());
        this.emailSubscription = BaseEnumCode.valueOf(YNCode.class, dto.getEmailSubscription());
        this.postalMailSubscription = BaseEnumCode.valueOf(YNCode.class, dto.getPostalMailSubscription());
        this.personalDataConsent = BaseEnumCode.valueOf(YNCode.class, dto.getPersonalDataConsent());
        this.personalDataConsentDate = dto.getPersonalDataConsentDate();

        // 코드 필드 업데이트
        this.codeGeneralPosition = positionCustomer;
        this.codeGeneralJob = jobCustomer;
        this.codeGeneralContactMethod = contactMethodCustomer;
        this.codeGeneralPurchaseDecisionFactor = purchaseDecisionFactorCustomer;
        this.codeGeneralInterestField = interestFieldCustomer;

        // 기타 필드 업데이트
        this.companyAddress = dto.getCompanyAddress();

        // 수정자 정보 업데이트
        this.modUserid = modifier;
        this.modDt = LocalDateTime.now(); // 현재 시간으로 수정 날짜 설정
        this.modIp = dto.getModIp();

        // 삭제 관련 필드 업데이트
        this.delUserid = dto.getDelUserid();
        this.delDt = dto.getDelDt();
        this.delIp = dto.getDelIp();
        if (dto.getDelDt() != null) {
            this.delYn = YNCode.Y;
        } else {
            this.delYn = YNCode.N;
        }
    }

}
