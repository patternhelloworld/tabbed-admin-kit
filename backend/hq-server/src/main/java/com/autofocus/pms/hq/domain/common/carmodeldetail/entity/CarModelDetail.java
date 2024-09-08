package com.autofocus.pms.hq.domain.common.carmodeldetail.entity;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.config.database.typeconverter.jpa.YNCodeConverter;
import com.autofocus.pms.hq.domain.common.carmodel.entity.CarModel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "car_model_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CarModelDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_model_detail_idx")
    private Integer carModelDetailIdx;

    @Column(name = "car_model_idx", insertable = false, updatable = false)
    private Integer carModelIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_model_idx")
    private CarModel carModel;


    @Column(name = "year")
    private Integer year;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "sht_name")
    private String shtName;

    @Column(name = "cg_idx")
    private Integer cgIdx;

    @Column(name = "motor_type")
    private String motorType;

    @Column(name = "car_name")
    private String carName;

    @Column(name = "gross_weight")
    private Integer grossWeight;

    @Column(name = "limit_man")
    private String limitMan;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Column(name = "cc")
    private Integer cc;

    @Column(name = "engine_room")
    private Integer engineRoom;

    @Column(name = "wheelbase")
    private Integer wheelbase;

    @Column(name = "power")
    private String power;

    @Column(name = "rpm")
    private Integer rpm;

    @Column(name = "size")
    private String size;

    @Column(name = "license")
    private Integer license;

    @Setter
    @Convert(converter = YNCodeConverter.class)
    @Column(name = "asbestos_yn")
    private YNCode asbestosYn;

    @Column(name = "auth_no")
    private String authNo;

    @Column(name = "jewon_no")
    private String jewonNo;

    @Column(name = "import_price", precision = 15, scale = 4)
    private BigDecimal importPrice;

    @Column(name = "view_yn")
    private String viewYn;

    @Setter
    @Convert(converter = YNCodeConverter.class)
    @Column(name = "stock_yn")
    private YNCode stockYn;

    @Column(name = "eng_type")
    private String engType;

    @Column(name = "lower_gas")
    private Byte lowerGas;

    @Column(name = "reg_user_id")
    private String regUserId;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "mod_user_id")
    private String modUserId;

    @Column(name = "mod_date")
    private LocalDateTime modDate;

    @Setter
    @Convert(converter = YNCodeConverter.class)
    @Column(name = "del_yn")
    private YNCode delYn;

    @Setter
    @Column(name = "del_date")
    private LocalDateTime delDate;

    @Setter
    @Column(name = "del_user_id")
    private String delUserId;
}
