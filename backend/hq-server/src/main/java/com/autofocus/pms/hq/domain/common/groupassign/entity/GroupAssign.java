package com.autofocus.pms.hq.domain.common.groupassign.entity;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.config.database.typeconverter.jpa.YNCodeConverter;
import com.autofocus.pms.hq.domain.common.customer.entity.Customer;
import com.autofocus.pms.hq.domain.common.customergroup.entity.CustomerGroup;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_assign")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GroupAssign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_assign_idx")
    private Integer groupAssignIdx;

    @Column(name = "customer_idx", insertable = false, updatable = false)
    private Integer customerIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_idx")
    private Customer customer;

    @Column(name = "customer_group_idx", insertable = false, updatable = false)
    private Integer customerGroupIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_group_idx")
    private CustomerGroup customerGroup;

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
}
