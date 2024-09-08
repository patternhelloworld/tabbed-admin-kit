package com.autofocus.pms.hq.domain.common.stock.entity;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.config.database.typeconverter.jpa.YNCodeConverter;
import com.autofocus.pms.hq.domain.common.vin.entity.Vin;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_idx")
    private Long stockIdx;

    @Column(name = "vin_idx", insertable = false, updatable = false)
    private Long vinIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vin_idx")
    private Vin vin;

    @Column(name = "reg_userid")
    private String regUserid;

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

    @Column(name = "del_ip")
    private String delIp;

    @Setter
    @Convert(converter = YNCodeConverter.class)
    @Column(name = "del_yn")
    private YNCode delYn;
}
