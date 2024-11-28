package io.github.patternhelloworld.tak.domain.common.carmodel.entity;

import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.config.database.typeconverter.jpa.YNCodeConverter;
import io.github.patternhelloworld.tak.domain.common.carmaker.entity.CarMaker;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "car_model")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_model_idx")
    private Integer carModelIdx;

    @Column(name = "car_maker_idx", insertable = false, updatable = false)
    private Integer carMakerIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_maker_idx")
    private CarMaker carMaker;

    @Column(name = "model_code")
    private String modelCode;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "svc_code")
    private String svcCode;

    @Column(name = "svc_name")
    private String svcName;

    @Column(name = "model_svc_name")
    private String modelSvcName;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "reg_user_id")
    private String regUserId;

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    @Column(name = "mod_user_id")
    private String modUserId;

    @Setter
    @Column(name = "del_dt")
    private LocalDateTime delDt;

    @Setter
    @Column(name = "del_user_id")
    private String delUserId;

    @Setter
    @Convert(converter = YNCodeConverter.class)
    @Column(name = "del_yn")
    private YNCode delYn;
}
