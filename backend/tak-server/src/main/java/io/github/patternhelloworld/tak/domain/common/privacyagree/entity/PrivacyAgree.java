package io.github.patternhelloworld.tak.domain.common.privacyagree.entity;

import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.config.database.typeconverter.jpa.YNCodeConverter;
import io.github.patternhelloworld.tak.domain.common.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "privacy_agree")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PrivacyAgree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "privacy_agree_idx")
    private Integer privacyAgreeIdx;

    @Column(name = "customer_idx", insertable = false, updatable = false)
    private Integer customerIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_idx")
    @NotFound(action = NotFoundAction.IGNORE)
    private Customer customer;

    @Convert(converter = YNCodeConverter.class)
    @Column(name = "is_agree")
    private YNCode isAgree;

    @Column(name = "fname")
    private String fname;

    @Column(name = "sname")
    private String sname;

    @Column(name = "agree_date")
    private Date agreeDate;

    @Column(name = "reg_userid")
    private String regUserId;

    @Column(name = "reg_dt")
    @CreationTimestamp
    private LocalDateTime regDt;

    @Column(name = "reg_ip")
    private String regIp;

    @Column(name = "mod_userid")
    private String modUserId;

    @Column(name = "mod_dt")
    @UpdateTimestamp
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
}
