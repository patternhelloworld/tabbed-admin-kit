package io.github.patternhelloworld.tak.domain.common.dept.entity;

import io.github.patternhelloworld.common.util.CustomUtils;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.dealer.entity.Dealer;
import io.github.patternhelloworld.tak.domain.common.dept.dto.DeptReqDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;


@Entity
@Table(name = "dept")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Dept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_idx")
    private Integer deptIdx;

    @Column(name = "dealer_cd", insertable = false, updatable = false)
    private Integer dealerCd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealer_cd")
    private Dealer dealer;


    @Column(name = "dept_nm")
    private String deptNm;

    @Column(name = "parent_cd")
    private Integer parentCd;

    @Column(name = "menu_num")
    private Integer menuNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "view_yn")
    private YNCode viewYn;

    @Column(name = "dept_sort")
    private Integer deptSort;

    @Column(name = "self_nm")
    private String selfNm;

    @Column(name = "self_biz_no")
    private String selfBizNo;

    @Column(name = "self_corporation_no")
    private String selfCorporationNo;

    @Column(name = "self_chief_nm")
    private String selfChiefNm;

    @Column(name = "self_uptae")
    private String selfUptae;

    @Column(name = "self_upjong")
    private String selfUpjong;

    @Column(name = "self_reg_id")
    private String selfRegId;

    @Column(name = "self_manager")
    private String selfManager;

    @Column(name = "self_hphone")
    private String selfHphone;

    @Column(name = "self_fax")
    private String selfFax;

    @Column(name = "self_zipcode")
    private String selfZipcode;

    @Column(name = "self_addr1")
    private String selfAddr1;

    @Column(name = "self_addr2")
    private String selfAddr2;

    @Column(name = "self_addr_si")
    private String selfAddrSi;

    @Column(name = "self_addr_gugun")
    private String selfAddrGugun;

    @Column(name = "self_addr_bname")
    private String selfAddrBname;

    @Column(name = "self_email")
    private String selfEmail;

    @Setter
    @Column(name = "self_img_fname")
    private String selfImgFname;

    @Column(name = "self_img_sname")
    private String selfImgSname;

    @Column(name = "reg_userid")
    private String regUserId;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "reg_ip")
    private String regIp;

    @Column(name = "mod_userid")
    private String modUserId;

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    @Column(name = "mod_ip")
    private String modIp;

    @Column(name = "del_userid")
    private String delUserId;

    @Column(name = "del_dt")
    private LocalDateTime delDt;

    @Column(name = "del_ip")
    private String delIp;

    @Enumerated(EnumType.STRING)
    @Column(name = "del_yn")
    private YNCode delYn;

    public void updateDept(DeptReqDTO.CreateUpdateOne dto, String modifier) {
        this.deptNm = dto.getDeptNm();
        this.deptSort = dto.getDeptSort();
        this.modUserId = modifier;
        this.modDt = LocalDateTime.now();
        this.selfNm = dto.getSelfNm();
        this.selfChiefNm = dto.getSelfChiefNm();
        this.selfBizNo = CustomUtils.encryptECB(dto.getSelfBizNo());
        this.selfRegId = CustomUtils.encryptECB(dto.getSelfRegId());
        this.selfUptae = dto.getSelfUptae();
        this.selfUpjong = dto.getSelfUpjong();
        this.selfZipcode = dto.getSelfZipcode();
        this.selfAddr1 = dto.getSelfAddr1();
        this.selfAddr2 = dto.getSelfAddr2();
        this.selfAddrSi = dto.getSelfAddrSi();
        this.selfAddrGugun = dto.getSelfAddrGugun();
        this.selfAddrBname = dto.getSelfAddrBname();
        this.selfImgFname = dto.getSelfImgFname();
        this.parentCd = dto.getParentCd();
    }
}
