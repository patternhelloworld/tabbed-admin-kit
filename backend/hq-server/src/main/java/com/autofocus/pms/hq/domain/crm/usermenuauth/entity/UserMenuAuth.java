package com.autofocus.pms.hq.domain.crm.usermenuauth.entity;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.crm.submenu.entity.SubMenu;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_menu_auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter
@Getter
/*@DynamicInsert
@DynamicUpdate*/
public class UserMenuAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_menu_auth_idx", nullable = false)
    private Long userMenuAuthIdx;

    @Enumerated(EnumType.STRING)
    @Column(name = "yn_lst", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private YNCode ynLst = YNCode.N;

    @Enumerated(EnumType.STRING)
    @Column(name = "yn_int", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private YNCode ynInt = YNCode.N;

    @Enumerated(EnumType.STRING)
    @Column(name = "yn_mod", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private YNCode ynMod = YNCode.N;

    @Enumerated(EnumType.STRING)
    @Column(name = "yn_del", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private YNCode ynDel = YNCode.N;

    @Enumerated(EnumType.STRING)
    @Column(name = "yn_xls", nullable = false, columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private YNCode ynXls = YNCode.N;

    @Column(name="sub_menu_idx", updatable = false, insertable = false)
    private Integer subMenuIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_menu_idx", nullable = false)
    private SubMenu subMenu;


    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "userid", nullable = false, columnDefinition = "VARCHAR(32) DEFAULT ''")
    private String userId = "";

    @Column(name = "reason", columnDefinition = "VARCHAR(255) DEFAULT NULL")
    private String reason;

    @Column(name = "dealer_cd", nullable = false, columnDefinition = "INT(10) DEFAULT 0")
    private Integer dealerCd = 0;

    @Column(name = "reg_userid", nullable = false, columnDefinition = "VARCHAR(32) DEFAULT ''")
    private String regUserId = "";

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "reg_ip", columnDefinition = "VARCHAR(20) DEFAULT ''")
    private String regIp = "";

    @Column(name = "mod_userid", columnDefinition = "VARCHAR(32) DEFAULT ''")
    private String modUserId = "";

    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    @Column(name = "mod_ip", columnDefinition = "VARCHAR(20) DEFAULT ''")
    private String modIp = "";

    @Column(name = "del_userid", columnDefinition = "VARCHAR(32) DEFAULT ''")
    private String delUserId = "";

    @Column(name = "del_dt")
    private LocalDateTime delDt;

    @Column(name = "del_ip", columnDefinition = "VARCHAR(20) DEFAULT ''")
    private String delIp = "";

    @Enumerated(EnumType.STRING)
    @Column(name = "del_yn", columnDefinition = "ENUM('Y','N') DEFAULT 'N'")
    private YNCode delYn = YNCode.N;

    // Getters and Setters
    // ...
}
