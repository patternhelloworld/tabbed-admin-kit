package com.autofocus.pms.hq.domain.common.customergroup.dto;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.customergroup.entity.CustomerGroup;
import com.autofocus.pms.hq.domain.common.dealer.entity.Dealer;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CustomerGroupReqDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateUpdateOne {

        @NotBlank(message = "그룹이름은 필수입니다.")
        private String groupNm;
        private String userid;
        private YNCode delYn;

        public CustomerGroup toEntity(Dealer dealer, User user) {
            CustomerGroup.CustomerGroupBuilder builder = CustomerGroup.builder()
                    .dealer(dealer)
                    .userid(user.getUserId())
                    .groupNm(this.groupNm)
                    .regUserid(user.getUserId())
                    .regIp("")
                    .delYn(this.delYn != null ? YNCode.Y : YNCode.N);;

            return builder.build();
        }
    }

}
