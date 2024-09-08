package com.autofocus.pms.hq.domain.common.carmaker.entity;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.config.database.typeconverter.jpa.YNCodeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "car_maker")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CarMaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_maker_idx")
    private Integer carMakerIdx;

    @Column(name = "car_maker_nm")
    private String carMakerNm;

    @Column(name = "brand_cd")
    private String brandCd;

    @Column(name = "img_fname")
    private String imgFname;

    @Column(name = "img_sname")
    private String imgSname;

    @Column(name = "instock_ld_idx")
    private Integer instockLdIdx;

    @Column(name = "pdi_end_ld_idx")
    private Integer pdiEndLdIdx;

    @Column(name = "i_check_cd")
    private String iCheckCd;

    @Column(name = "c_check_cd")
    private String cCheckCd;

    @Column(name = "check_cycle")
    private Integer checkCycle;

    @Column(name = "check_unit")
    private String checkUnit;

    @Column(name = "is_part")
    private String isPart;

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
