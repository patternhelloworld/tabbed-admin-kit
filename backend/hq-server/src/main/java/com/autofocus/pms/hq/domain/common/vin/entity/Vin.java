package com.autofocus.pms.hq.domain.common.vin.entity;


import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.config.database.typeconverter.jpa.YNCodeConverter;
import com.autofocus.pms.hq.domain.common.carmodeldetail.entity.CarModelDetail;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import com.autofocus.pms.hq.domain.common.vin.dto.VinCommonDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vin")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Vin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vin_idx")
    private Long vinIdx;

    // 차량 재원
    @Column(name = "car_model_detail_idx", insertable = false, updatable = false)
    private Integer carModelDetailIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_model_detail_idx")
    private CarModelDetail carModelDetail;


    @Column(name = "vin_number")
    private String vinNumber;

    @Column(name = "reg_userid", insertable = false, updatable = false)
    private String regUserid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reg_userid", referencedColumnName = "user_id")
    private User regUser;

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

    public void updateVin(VinCommonDTO.One dto, CarModelDetail carModelDetail, String modifier){

        this.carModelDetail = carModelDetail;
        this.vinNumber = dto.getVinNumber();

        // 수정자 정보 업데이트
        this.modUserid = modifier;
        this.modDt = LocalDateTime.now(); // 현재 시간으로 수정 날짜 설정
        this.modIp = dto.getModIp();

        // 삭제 관련 필드 업데이트
        this.delUserid = dto.getDelUserid();
        this.delDt = dto.getDelDt();
        this.delIp = dto.getDelIp();
        if (dto.getDelDt() != null) {
            this.delYn = YNCode.Y;
        } else {
            this.delYn = YNCode.N;
        }
    }
}
