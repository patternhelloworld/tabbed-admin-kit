package io.github.patternhelloworld.tak.domain.common.dealerstock.entity;


import io.github.patternhelloworld.tak.config.database.typeconverter.BaseEnumCode;
import io.github.patternhelloworld.tak.config.database.typeconverter.DealerStockUseType;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.config.database.typeconverter.jpa.DealerStockUseTypeConverter;
import io.github.patternhelloworld.tak.config.database.typeconverter.jpa.YNCodeConverter;
import io.github.patternhelloworld.tak.domain.common.dealerstock.dto.DealerStockCommonDTO;
import io.github.patternhelloworld.tak.domain.common.dept.entity.Dept;
import io.github.patternhelloworld.tak.domain.common.stock.entity.Stock;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dealer_stock")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DealerStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dealer_stock_idx")
    private Long dealerStockIdx;

    // 본사 재고
    @Column(name = "stock_idx", insertable = false, updatable = false)
    private Long stockIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_idx")
    private Stock stock;


    // 딜러
/*    @Column(name = "dealer_cd", insertable = false, updatable = false)
    private Integer dealerCd;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealer_cd")
    private Dealer dealer;*/

    // 전시장 위치
    @Column(name = "dept_idx", insertable = false, updatable = false)
    private Integer deptIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_idx")
    private Dept dept;


    @Column(name = "use_type")
    @Convert(converter = DealerStockUseTypeConverter.class)
    private DealerStockUseType useType;

    @Column(name = "import_date")
    private LocalDate importDate;

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


    public void updateDealerStock(DealerStockCommonDTO.One dto, Dept dept, String modifier){
        this.useType = BaseEnumCode.valueOf(DealerStockUseType.class, dto.getUseType());
        this.importDate = dto.getImportDate();
        this.dept = dept;

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
