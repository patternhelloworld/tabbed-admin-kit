package com.autofocus.pms.hq.domain.common.relocatelog.entity;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.config.database.typeconverter.jpa.YNCodeConverter;
import com.autofocus.pms.hq.domain.common.customer.entity.Customer;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "relocate_log")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RelocateLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relocate_log_idx")
    private Integer relocateLogIdx;

    @Column(name = "customer_idx", insertable = false, updatable = false)
    private Long customerIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_idx", referencedColumnName = "customer_idx")
    private Customer customer;

    @Column(name = "from_user_manager_idx", insertable = false, updatable = false)
    private Long fromUserManagerIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_manager_idx", referencedColumnName = "user_idx")
    private User fromUserManager;

    @Column(name = "to_user_manager_idx", insertable = false, updatable = false)
    private Long toUserManagerIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_manager_idx", referencedColumnName = "user_idx")
    private User toUserManager;

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
