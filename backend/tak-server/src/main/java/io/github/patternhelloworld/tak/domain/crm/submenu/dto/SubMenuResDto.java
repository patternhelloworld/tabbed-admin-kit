package io.github.patternhelloworld.tak.domain.crm.submenu.dto;

import io.github.patternhelloworld.tak.domain.crm.submenu.entity.SubMenu;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class SubMenuResDto {

    @Getter
    @NoArgsConstructor
    public static class OneWithMainMenu {

        private Integer subMenuIdx;

        private Integer mainMenuIdx;
        private String mainMenuNm;
        private String mainMenuKey;
        private String mainMenuPath;

        private String subMenuNm;
        private String subMenuKey;

        private String subMenuPath;
        private String subMenuGb;
        private Integer subMenuSort;
        private String dlrMenuAuthYn;
        private String regUserId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;
        private String regIp;
        private String modUserId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modDt;
        private String modIp;
        private String delUserId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime delDt;
        private String delIp;
        private String delYn;


        @QueryProjection
        public OneWithMainMenu(Integer subMenuIdx, Integer mainMenuIdx, String mainMenuNm, String mainMenuKey, String mainMenuPath, String subMenuNm, String subMenuKey, String subMenuPath, String subMenuGb, Integer subMenuSort, String dlrMenuAuthYn, String regUserId, LocalDateTime regDt, String regIp, String modUserId, LocalDateTime modDt, String modIp, String delUserId, LocalDateTime delDt, String delIp, String delYn) {
            this.subMenuIdx = subMenuIdx;
            this.mainMenuIdx = mainMenuIdx;
            this.mainMenuNm = mainMenuNm;
            this.mainMenuKey = mainMenuKey;
            this.mainMenuPath =mainMenuPath;
            this.subMenuNm = subMenuNm;
            this.subMenuKey = subMenuKey;
            this.subMenuPath = subMenuPath;
            this.subMenuGb = subMenuGb;
            this.subMenuSort = subMenuSort;
            this.dlrMenuAuthYn = dlrMenuAuthYn;
            this.regUserId = regUserId;
            this.regDt = regDt;
            this.regIp = regIp;
            this.modUserId = modUserId;
            this.modDt = modDt;
            this.modIp = modIp;
            this.delUserId = delUserId;
            this.delDt = delDt;
            this.delIp = delIp;
            this.delYn = delYn;
        }
    }


    @Getter
    public static class MinimalOne {

        private Integer subMenuIdx;
        private Integer mainMenuIdx;
        private String subMenuNm;
        private String subMenuPath;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime delDt;


        @QueryProjection
        public MinimalOne(Integer subMenuIdx, Integer mainMenuIdx, String subMenuNm, String subMenuPath, LocalDateTime delDt) {
            this.subMenuIdx = subMenuIdx;
            this.mainMenuIdx = mainMenuIdx;
            this.subMenuNm = subMenuNm;
            this.subMenuPath = subMenuPath;
            this.delDt = delDt;
        }
    }

    @Getter
    public static class Id {
        private Integer id;

        public Id(Integer id) {
            this.id = id;
        }
        public Id(SubMenu subMenu) {
            this.id = subMenu.getSubMenuIdx();
        }
    }


}
