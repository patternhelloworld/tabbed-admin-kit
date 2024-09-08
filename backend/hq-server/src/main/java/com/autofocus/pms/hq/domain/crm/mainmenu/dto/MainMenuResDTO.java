package com.autofocus.pms.hq.domain.crm.mainmenu.dto;

import com.autofocus.pms.hq.domain.crm.mainmenu.entity.MainMenu;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

public class MainMenuResDTO {

    @Getter
    @NoArgsConstructor
    public static class One {
        private Integer mainMenuIdx;
        private String mainMenuNm;
        private String mainMenuPath;
        private String isTable;
        private Integer mainMenuSort;
        private String mainMenuIcon;
        private String regUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp regDt;
        private String regIp;
        private String modUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp modDt;
        private String modIp;

        @QueryProjection
        public One(Integer mainMenuIdx, String mainMenuNm, String mainMenuPath, String isTable, Integer mainMenuSort, String mainMenuIcon
                        , String regUserid, Timestamp regDt, String regIp
                        , String modUserid, Timestamp modDt, String modIp) {
            this.mainMenuIdx = mainMenuIdx;
            this.mainMenuNm = mainMenuNm;
            this.mainMenuPath = mainMenuPath;
            this.isTable = isTable;
            this.mainMenuSort = mainMenuSort;
            this.mainMenuIcon = mainMenuIcon;
            this.regUserid = regUserid;
            this.regDt = regDt;
            this.regIp = regIp;
            this.modUserid = modUserid;
            this.modDt = modDt;
            this.modIp = modIp;
        }

        public One(MainMenu mainMenu){
            this.mainMenuIdx = mainMenu.getMainMenuIdx();
            this.mainMenuNm = mainMenu.getMainMenuNm();
            this.mainMenuPath = mainMenu.getMainMenuPath();
            this.isTable = mainMenu.getIsTable();
            this.mainMenuSort = mainMenu.getMainMenuSort();
            this.mainMenuIcon = mainMenu.getMainMenuIcon();
            this.regUserid = mainMenu.getRegUserid();
            this.regDt = mainMenu.getRegDt();
            this.regIp = mainMenu.getRegIp();
            this.modUserid = mainMenu.getModUserid();
            this.modDt = mainMenu.getModDt();
            this.modIp = mainMenu.getModIp();
        }
    }


    @Getter
    @NoArgsConstructor
    public static class MinimalOne {
        private Integer mainMenuIdx;
        private String mainMenuNm;
        private String mainMenuPath;

        public MinimalOne(Integer mainMenuIdx, String mainMenuNm, String mainMenuPath) {
            this.mainMenuIdx = mainMenuIdx;
            this.mainMenuNm = mainMenuNm;
            this.mainMenuPath = mainMenuPath;
        }
    }

    @Getter
    public static class MainMenuIdx {
        private final Integer mainMenuIdx;

        public MainMenuIdx(MainMenu mainMenu)
        {
            this.mainMenuIdx = mainMenu.getMainMenuIdx();
        }
    }


}
