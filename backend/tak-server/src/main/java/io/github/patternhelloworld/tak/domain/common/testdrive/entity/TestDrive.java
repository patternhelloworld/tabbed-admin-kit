package io.github.patternhelloworld.tak.domain.common.testdrive.entity;

import io.github.patternhelloworld.tak.config.database.typeconverter.ApprovalStatus;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.config.database.typeconverter.jpa.ApprovalStatusConverter;
import io.github.patternhelloworld.tak.config.database.typeconverter.jpa.YNCodeConverter;
import io.github.patternhelloworld.tak.domain.common.customer.entity.Customer;
import io.github.patternhelloworld.tak.domain.common.testdrive.dto.TestDriveCommonDTO;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;
import io.github.patternhelloworld.tak.domain.common.vin.entity.Vin;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_drive")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TestDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_drive_idx")
    private Long testDriveIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vin_idx", referencedColumnName = "vin_idx")
    private Vin vin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", referencedColumnName = "user_idx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_idx", referencedColumnName = "customer_idx")
    private Customer customer;

    @Column(name = "car_no")
    private String carNo;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "start_mile")
    private Integer startMile;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "end_mile")
    private Integer endMile;

    @Column(name = "test_place")
    private String testPlace;

    @Column(name = "remain_fuel")
    private String remainFuel;

    @Column(name = "mileage")
    private String mileage;

    @Column(name = "fuel_fee")
    private Integer fuelFee;

    @Column(name = "wash_fee")
    private Integer washFee;

    @Column(name = "comment")
    private String comment;

    @Setter
    @Convert(converter = ApprovalStatusConverter.class)
    @Column(name = "is_approved")
    private ApprovalStatus isApproved;

    @Setter
    @Convert(converter = YNCodeConverter.class)
    @Column(name = "is_drive")
    private YNCode isDrive;

    @Column(name = "reg_userid")
    private String regUserid;

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "mod_userid")
    private String modUserid;

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    @Setter
    @Column(name = "del_userid")
    private String delUserid;

    @Setter
    @Column(name = "del_dt")
    private LocalDateTime delDt;

    @Setter
    @Convert(converter = YNCodeConverter.class)
    @Column(name = "del_yn")
    private YNCode delYn;


    public void updateTestDrive(TestDriveCommonDTO.One dto, Vin vin, User user, Customer customer, String modifier) {
        this.vin = vin;
        this.user = user;
        this.customer = customer;
        this.carNo = dto.getCarNo();
        this.startDate = dto.getStartDate();
        this.startMile = dto.getStartMile();
        this.endDate = dto.getEndDate();
        this.endMile = dto.getEndMile();
        this.testPlace = dto.getTestPlace();
        this.remainFuel = dto.getRemainFuel();
        this.mileage = dto.getMileage();
        this.fuelFee = dto.getFuelFee();
        this.washFee = dto.getWashFee();
        this.comment = dto.getComment();

        this.isApproved = dto.getIsApproved();
        this.isDrive = dto.getIsDrive();

        // 수정자 정보 업데이트
        this.modUserid = modifier;
        this.modDt = LocalDateTime.now(); // 현재 시간으로 수정 날짜 설정
    }
}
