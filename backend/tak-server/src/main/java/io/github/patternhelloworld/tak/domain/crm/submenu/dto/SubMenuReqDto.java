package io.github.patternhelloworld.tak.domain.crm.submenu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SubMenuReqDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Update {

        @NotBlank(message = "서브메뉴명은 비어있으면 안됩니다.")
        @NotNull(message = "서브메뉴명은 비어있으면 안됩니다.")
        private String subMenuNm;
        @NotBlank(message = "서브메뉴경로는 비어있으면 안됩니다.")
        @NotNull(message = "서브메뉴경로는 비어있으면 안됩니다.")
        public String subMenuPath;
        @NotNull(message = "서브메뉴 소트는 비어있으면 안됩니다.")
        private Integer subMenuSort;
    }

}
