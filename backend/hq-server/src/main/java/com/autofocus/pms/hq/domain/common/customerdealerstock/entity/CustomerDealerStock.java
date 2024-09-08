package com.autofocus.pms.hq.domain.common.customerdealerstock.entity;


import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.config.database.typeconverter.jpa.YNCodeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_dealer_stock")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CustomerDealerStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_dealer_stock_idx")
    private Long customerDealerStockIdx;


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
    @Setter
    @Column(name = "del_ip")
    private String delIp;
    @Setter
    @Convert(converter = YNCodeConverter.class)
    @Column(name = "del_yn")
    private YNCode delYn;
}
