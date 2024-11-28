package io.github.patternhelloworld.tak.domain.common.approvalline.dto;

import io.github.patternhelloworld.tak.config.database.typeconverter.LineGb;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.approvalline.entity.ApprovalLine;
import io.github.patternhelloworld.tak.domain.common.dealer.entity.Dealer;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class ApprovalLineReqDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateUpdateOne {
        private Integer approvalLineIdx;
        private Dealer dealer;

        @NotNull(message = "전시장은 필수입니다.")
        private Integer showroomIdx;

        @NotNull(message = "결재 구분은 필수입니다.")
        private LineGb lineGb;

        private String lineDetails;
        private String regUserid;
        private String regIp;
        private String modUserid;
        private YNCode delYn;

        public ApprovalLine toEntity(Dealer dealer) {
            ApprovalLine.ApprovalLineBuilder builder = ApprovalLine.builder()
                    .dealer(dealer)
                    .showroomIdx(this.showroomIdx)
                    .lineGb(this.lineGb)
                    .lineDetails(this.lineDetails)
                    .regUserid(dealer.getDealerNm())
                    .regIp(this.regIp)
                    .modUserid(this.modUserid)
                    .delYn(this.delYn);

            return builder.build();
        }
    }

}
