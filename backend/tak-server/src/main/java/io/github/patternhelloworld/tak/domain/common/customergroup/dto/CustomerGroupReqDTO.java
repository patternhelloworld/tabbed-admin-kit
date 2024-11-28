package io.github.patternhelloworld.tak.domain.common.customergroup.dto;

import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.customergroup.entity.CustomerGroup;
import io.github.patternhelloworld.tak.domain.common.dealer.entity.Dealer;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;
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
