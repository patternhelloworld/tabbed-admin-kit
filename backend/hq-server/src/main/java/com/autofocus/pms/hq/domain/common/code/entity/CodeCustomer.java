package com.autofocus.pms.hq.domain.common.code.entity;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.config.database.typeconverter.jpa.YNCodeConverter;
import com.autofocus.pms.hq.domain.common.code.dto.CodeCustomerReqDTO;
import com.autofocus.pms.hq.domain.common.dealer.entity.Dealer;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "code_customer")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CodeCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_customer_idx")
    private Integer codeCustomerIdx;

    @Column(name = "category_cd")
    private String categoryCd;

    @Column(name = "category_nm")
    private String categoryNm;

    @Setter
    @Convert(converter = YNCodeConverter.class)
    @Column(name = "category_yn")
    private YNCode categoryYn;

    @Column(name = "parent")
    private Integer parent;

    @Column(name = "code_customer_idx2")
    private Integer codeCustomerIdx2;

    @Column(name = "code_customer_nm")
    private String codeCustomerNm;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "dealer_cd", insertable = false, updatable = false)
    private Integer dealerCd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealer_cd")
    @NotFound(action = NotFoundAction.IGNORE)
    private Dealer dealer;

    @Column(name = "reg_userid")
    private String regUserid;

    @Column(name = "reg_dt")
    @CreationTimestamp
    private LocalDateTime regDt;

    @Column(name = "reg_ip")
    private String regIp;

    @Column(name = "mod_userid")
    private String modUserid;

    @Column(name = "mod_dt")
    @UpdateTimestamp
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

    public void updateCodeCustomer(CodeCustomerReqDTO.CreateUpdateOne dto, Dealer dealer, String userid) {
        this.categoryCd = dto.getCategoryCd();
        this.categoryNm = dto.getCategoryNm();
        this.modUserid = userid;
    }

    public void updateMetaCustomerCode(CodeCustomerReqDTO.MetaCreateUpdateOne dto, String userid) {
        this.codeCustomerNm = dto.getCodeCustomerNm();
        this.sort = dto.getSort();
        this.modUserid = userid;
    }
}
