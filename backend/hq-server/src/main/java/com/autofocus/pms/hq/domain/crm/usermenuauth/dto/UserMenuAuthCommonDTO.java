package com.autofocus.pms.hq.domain.crm.usermenuauth.dto;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.crm.submenu.entity.SubMenu;
import com.autofocus.pms.hq.domain.crm.usermenuauth.entity.UserMenuAuth;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import com.querydsl.core.annotations.QueryProjection;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UserMenuAuthCommonDTO {

    @Getter
    public static class PermissionSet {
        private Long copyToUserIdx;
        private List<OneWithSubMenu> permissions;
    }


    @Getter
    @Setter
    @Builder
    public static class Permission {
        private YNCode ynLst;
        private YNCode ynInt;
        private YNCode ynMod;
        private YNCode ynDel;
        private YNCode ynXls;
        private String subMenuNm;
        private String subMenuPath;
        private String subMenuKey;
        private String mainMenuNm;
        private String mainMenuPath;
        private String mainMenuKey;

        @QueryProjection
        public Permission(YNCode ynLst, YNCode ynInt, YNCode ynMod, YNCode ynDel, YNCode ynXls, String subMenuNm, String subMenuPath, String subMenuKey, String mainMenuNm, String mainMenuPath, String mainMenuKey) {
            this.ynLst = ynLst;
            this.ynInt = ynInt;
            this.ynMod = ynMod;
            this.ynDel = ynDel;
            this.ynXls = ynXls;
            this.subMenuNm = subMenuNm;
            this.subMenuPath = subMenuPath;
            this.subMenuKey = subMenuKey;
            this.mainMenuNm = mainMenuNm;
            this.mainMenuPath = mainMenuPath;
            this.mainMenuKey = mainMenuKey;
        }
    }


    @Getter
    @Setter
    @Builder
    public static class OneWithSubMenu {
        private Long userMenuAuthIdx;
        private YNCode ynLst;
        private YNCode ynInt;
        private YNCode ynMod;
        private YNCode ynDel;
        private YNCode ynXls;

        private Integer subMenuIdx;
        private String subMenuNm;

        private Integer mainMenuIdx;
        private String mainMenuNm;

        private Long userIdx;
        private String userId;
        private String reason;
        private Integer dealerCd;
        private String regUserId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;
        private String regIp;
        @Setter
        private String modUserId;
        @Setter
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modDt;
        private String modIp;
        private String delUserId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime delDt;
        private String delIp;
        private YNCode delYn;


        @QueryProjection
        public OneWithSubMenu(Long userMenuAuthIdx, YNCode ynLst, YNCode ynInt, YNCode ynMod, YNCode ynDel, YNCode ynXls, Integer subMenuIdx, String subMenuNm, Integer mainMenuIdx, String mainMenuNm, Long userIdx, String userId, String reason, Integer dealerCd, String regUserId, LocalDateTime regDt, String regIp, String modUserId, LocalDateTime modDt, String modIp, String delUserId, LocalDateTime delDt, String delIp, YNCode delYn) {
            this.userMenuAuthIdx = userMenuAuthIdx;
            this.ynLst = ynLst;
            this.ynInt = ynInt;
            this.ynMod = ynMod;
            this.ynDel = ynDel;
            this.ynXls = ynXls;
            this.subMenuIdx = subMenuIdx;
            this.subMenuNm = subMenuNm;
            this.mainMenuIdx = mainMenuIdx;
            this.mainMenuNm = mainMenuNm;
            this.userIdx = userIdx;
            this.userId = userId;
            this.reason = reason;
            this.dealerCd = dealerCd;
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



        public UserMenuAuth toEntity(SubMenu subMenu) {
            return UserMenuAuth.builder()
                    .userMenuAuthIdx(this.userMenuAuthIdx)
                    .ynLst(this.ynLst)
                    .ynInt(this.ynInt)
                    .ynMod(this.ynMod)
                    .ynDel(this.ynDel)
                    .ynXls(this.ynXls)

                    .subMenu(subMenu)

                    .userIdx(this.userIdx)
                    .userId(this.userId)
                    .reason(this.reason)
                    .dealerCd(this.dealerCd)
                    .regUserId(this.regUserId)
                    .regDt(this.regDt)
                    .regIp(this.regIp)
                    .modUserId(this.modUserId)
                    .modDt(this.modDt)
                    .modIp(this.modIp)
                    .delUserId(this.delUserId)
                    .delDt(this.delDt)
                    .delIp(this.delIp)
                    .delYn(this.delYn)
                    .build();
        }
    }


    // Route 를 서버에서 리턴



}
