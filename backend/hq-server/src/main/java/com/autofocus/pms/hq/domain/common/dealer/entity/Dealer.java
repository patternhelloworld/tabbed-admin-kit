package com.autofocus.pms.hq.domain.common.dealer.entity;


import com.autofocus.pms.hq.config.database.typeconverter.DealerGb;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.dealer.dto.DealerReqDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dealer")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Dealer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dealer_cd")
    private Integer dealerCd;

    @Enumerated(EnumType.STRING)
    @Column(name = "dealer_gb")
    private DealerGb dealerGb;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_main")
    private YNCode isMain;

    @Column(name = "dealer_nm")
    private String dealerNm;

    @Column(name = "short_nm")
    private String shortNm;

    @Column(name = "tel")
    private String tel;

    @Column(name = "hphone")
    private String hphone;

    @Column(name = "fax")
    private String fax;

    @Column(name = "chief_nm")
    private String chiefNm;

    @Column(name = "email")
    private String email;

    @Column(name = "uptae")
    private String uptae;

    @Column(name = "upjong")
    private String upjong;

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

    @Column(name = "pdi_request_count")
    private String pdiRequestCount;

    @Column(name = "reg_userid")
    private String regUserid;

    @Column(name="reg_dt", updatable = false)
    @CreationTimestamp
    private LocalDateTime regDt;

    @Column(name = "reg_ip")
    private String regIp;

    @Column(name = "mod_userid")
    private String modUserid;

    @Column(name="mod_dt")
    @UpdateTimestamp
    private LocalDateTime modDt;

    @Column(name = "mod_ip")
    private String modIp;

    @Column(name = "del_userid")
    private String delUserid;

    @Column(name="del_dt")
    private LocalDateTime delDt;

    @Column(name = "del_ip")
    private String delIp;

    @Enumerated(EnumType.STRING)
    @Column(name = "del_yn")
    private YNCode delYn = YNCode.N;
    @PrePersist
    protected void onCreate() {
        if (delYn == null) {
            delYn = YNCode.N; // 기본값 설정
        }
    }
    @PreUpdate
    protected void onUpdate() {
        if (delYn == null) {
            delYn = YNCode.N; // 업데이트 시에도 기본값 설정
        }
    }

    public void updateDealer(DealerReqDTO.CreateUpdateOne dto) {
        this.dealerGb = dto.getDealerGb();
        this.isMain = dto.getIsMain();
        this.dealerNm = dto.getDealerNm();
        this.shortNm = dto.getShortNm();
        this.tel = dto.getTel();
        this.hphone = dto.getHphone();
        this.fax = dto.getFax();
        this.chiefNm = dto.getChiefNm();
        this.email = dto.getEmail();
        this.uptae = dto.getUptae();
        this.upjong = dto.getUpjong();
        this.zipcode = dto.getZipcode();
        this.addr1 = dto.getAddr1();
        this.addr2 = dto.getAddr2();
        this.addrSi = dto.getAddrSi();
        this.addrGugun = dto.getAddrGugun();
        this.addrBname = dto.getAddrBname();
        this.pdiRequestCount = dto.getPdiRequestCount();
    }
}
