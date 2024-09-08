package com.autofocus.pms.hq.domain.common.code.dto;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.code.entity.CodeCustomer;
import com.autofocus.pms.hq.domain.common.dealer.entity.Dealer;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CodeCustomerReqDTO {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateUpdateOne {
        @NotBlank(message = "카테고리 코드는 필수입니다.")
        private String categoryCd;
        
        @NotBlank(message = "카테고리 이름은 필수입니다.")
        private String categoryNm;
        private YNCode delYn;

        public CodeCustomer toEntity(Dealer dealer, User user) {
            CodeCustomer.CodeCustomerBuilder builder = CodeCustomer.builder()
                    .categoryCd(this.categoryCd)
                    .categoryNm(this.categoryNm)
                    .categoryYn(YNCode.Y)
                    .regUserid(user.getUserId())
                    .delYn(this.delYn != null ? YNCode.Y : YNCode.N);

            return builder.build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MetaCreateUpdateOne {
        @NotBlank(message = "신규 코드 이름은 필수입니다.")
        private String codeCustomerNm;
        private Integer sort;
        private YNCode delYn;

        public CodeCustomer toEntity(Integer parentIdx, User user) {
            CodeCustomer.CodeCustomerBuilder builder = CodeCustomer.builder()
                    .codeCustomerNm(this.codeCustomerNm)
                    .sort(this.sort)
                    .parent(parentIdx)
                    .categoryYn(YNCode.N)
                    .regUserid(user.getUserId())
                    .delYn(this.delYn != null ? YNCode.Y : YNCode.N);
            return builder.build();
        }
    }
}
