package com.autofocus.pms.hq.domain.common.approvalline.dto;

import com.autofocus.pms.hq.config.database.typeconverter.LineGb;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.approvalline.entity.ApprovalLine;
import com.autofocus.pms.hq.domain.common.dept.entity.Dept;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApprovalLineResDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class One {
        private Integer deptIdx;
        private String deptNm;
        private Integer approvalLineIdx;
        private Integer dealerCd;
        private Integer showroomIdx;
        private LineGb lineGb;
        private String lineDetails;
        private String regUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime regDt;

        private String modUserid;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modDt;

        private String lineGbStr;
        private Map<Integer, List<String>> userInfos;

        @QueryProjection
        public One(Integer deptIdx, String deptNm, Integer approvalLineIdx, Integer dealerCd, Integer showroomIdx, LineGb lineGb, String lineDetails, String regUserid, LocalDateTime regDt, String modUserid, LocalDateTime modDt) {
            this.deptIdx = deptIdx;
            this.deptNm = deptNm;
            this.approvalLineIdx = approvalLineIdx;
            this.dealerCd = dealerCd;
            this.showroomIdx = showroomIdx;
            this.lineGb = lineGb;
            this.lineDetails = lineDetails;
            this.regUserid = regUserid;
            this.regDt = regDt;
            this.modUserid = modUserid;
            this.modDt = modDt;
            this.lineGbStr = lineGb.getValue();
            this.userInfos = new HashMap<>();
        }
        public void addUser(Integer index, List<String> userDetails) {
            if (userInfos == null) {
                userInfos = new HashMap<>();
            }
            userInfos.put(index, userDetails);
        }
    }

    @Getter
    public static class ApprovalLineIdx {
        private Integer approvalLineIdx;
        public ApprovalLineIdx(ApprovalLine approvalLine) { this.approvalLineIdx = approvalLine.getApprovalLineIdx(); }
        public ApprovalLineIdx(Integer approvalLineIdx) { this.approvalLineIdx = approvalLineIdx; }
    }

    @Getter
    public static class DeptAndLineGbs {
        private Integer deptIdx;
        private String deptNm;
        private Integer lineA;
        private Integer lineC;
        private Integer lineD;

        @QueryProjection
        public DeptAndLineGbs(Integer deptIdx, String deptNm, Integer lineA, Integer lineC, Integer lineD) {
            this.deptIdx = deptIdx;
            this.deptNm = deptNm;
            this.lineA = lineA;
            this.lineC = lineC;
            this.lineD = lineD;
        }
    }
}
