package com.autofocus.pms.hq.domain.common.customergroup.entity;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.config.database.typeconverter.jpa.YNCodeConverter;
import com.autofocus.pms.hq.domain.common.customergroup.dto.CustomerGroupReqDTO;
import com.autofocus.pms.hq.domain.common.dealer.entity.Dealer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_group")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CustomerGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_group_idx")
    private Integer customerGroupIdx;

    @Column(name = "dealer_cd", insertable = false, updatable = false)
    private Integer dealerCd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealer_cd")
    private Dealer dealer;

    @Column(name = "userid")
    private String userid;

    @Column(name = "group_nm")
    private String groupNm;

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

    public void updateCustomerGroup(CustomerGroupReqDTO.CreateUpdateOne dto, String userid) {
        this.groupNm = dto.getGroupNm();
        this.userid = dto.getUserid();
        this.modUserid = userid;
    }
}
