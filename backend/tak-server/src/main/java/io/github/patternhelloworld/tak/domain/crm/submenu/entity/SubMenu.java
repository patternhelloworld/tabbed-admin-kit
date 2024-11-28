package io.github.patternhelloworld.tak.domain.crm.submenu.entity;

import io.github.patternhelloworld.tak.domain.crm.mainmenu.entity.MainMenu;
import io.github.patternhelloworld.tak.domain.crm.submenu.dto.SubMenuReqDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "sub_menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class SubMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_menu_idx", nullable = false)
    private Integer subMenuIdx;

    @Column(name="main_menu_idx",nullable = false, insertable = false, updatable = false)
    private Integer mainMenuIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_menu_idx")
    private MainMenu mainMenu;

    @Column(name = "sub_menu_nm", nullable = false)
    private String subMenuNm;

    @Column(name="sub_menu_key")
    private String subMenuKey;

    @Column(name = "sub_menu_path", nullable = false)
    private String subMenuPath;

    @Column(name = "sub_menu_gb", nullable = false)
    private String subMenuGb;

    @Column(name = "sub_menu_sort")
    private Integer subMenuSort;

    @Column(name = "dlr_menu_auth_yn", nullable = false)
    private String dlrMenuAuthYn;

    @Column(name = "reg_userid", updatable = false)
    private String regUserId;

    @Column(name = "reg_dt", updatable = false)
    @CreationTimestamp
    private LocalDateTime regDt;

    @Column(name = "reg_ip", updatable = false)
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
    @UpdateTimestamp
    private LocalDateTime delDt;

    @Column(name = "del_ip")
    private String delIp;

    @Column(name="del_yn")
    private String delYn;


    public void updateSubMenu(SubMenuReqDto.Update dto) {
        this.subMenuNm = dto.getSubMenuNm();
        this.subMenuPath = dto.getSubMenuPath();
        this.subMenuSort = dto.getSubMenuSort();
    }
}
