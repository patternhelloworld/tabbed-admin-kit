package com.autofocus.pms.hq.domain.crm.mainmenu.entity;

import com.autofocus.pms.hq.domain.crm.mainmenu.dto.MainMenuReqDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name="main_menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class MainMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "main_menu_idx", nullable = false, updatable = false)
    private Integer mainMenuIdx;

    @Column(name="main_menu_nm")
    private String mainMenuNm;

    @Column(name="main_menu_key")
    private String mainMenuKey;

    @Column(name="main_menu_path")
    private String mainMenuPath;

    @Column(name="is_table")
    private String isTable;

    @Column(name="main_menu_sort")
    private Integer mainMenuSort;

    @Column(name="main_menu_icon")
    private String mainMenuIcon;

    @Column(name="reg_userid", updatable = false)
    private String regUserid;

    @Column(name="reg_dt", updatable = false)
    @CreationTimestamp
    private Timestamp regDt;

    @Column(name="reg_ip", updatable = false)
    private String regIp;

    @Column(name="mod_userid")
    private String modUserid;

    @Column(name="mod_dt")
    @UpdateTimestamp
    private Timestamp modDt;

    @Column(name="mod_ip")
    private String modIp;

    @Column(name="del_userid")
    private String delUserid;

    @Column(name="del_dt")
    @UpdateTimestamp
    private Timestamp delDt;

    @Column(name="del_ip")
    private String delIp;

    @Column(name="del_yn")
    private String delYn;

    public void updateNameSort(MainMenuReqDTO.UpdateOne dto) {
        this.mainMenuNm = dto.getMainMenuNm();
        this.mainMenuPath = dto.getMainMenuPath();
        this.mainMenuSort = dto.getMainMenuSort();
    }
}
