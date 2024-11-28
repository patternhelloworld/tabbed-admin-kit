package io.github.patternhelloworld.tak.domain.common.extcode.dto;

import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.extcode.entity.ExtCode;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ExtCodeReqDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateUpdateOne{
        @NotBlank(message = "코드는 필수입니다.")
        private String code;
        private String description;
        private YNCode delYn;

        public ExtCode toEntity(User user) {
            ExtCode.ExtCodeBuilder builder = ExtCode.builder()
                    .code(this.code)
                    .description(this.description)
                    .regUserId(user.getUserId())
                    .delYn(this.delYn != null ? YNCode.Y : YNCode.N);

            return builder.build();
        }
    }

}
