package io.github.patternhelloworld.tak.domain.crm.mainmenu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/*
*
*   아래의 유효성 검사는 pms-common 의 PmsCommonExceptionHandler 에서 처리 합니다.
*
* */
public class MainMenuReqDTO {

    @Getter
    public static class UpdateOne {

        // message 는 프론트 엔드의 화면에 userMessage 로 출력 됩니다.
        // @NotBlank 는 빈 스트링 까지 잡습니다. 공백만 있는 경우도 잡습니다. 단, 숫자 타입에는 사용할 수 없습니다. 숫자 타입은, @NotNull 을 사용해주세요.
        // 프론트엔드에서 formik 으로 유효성 검사를 하지만, API는 독립적으로 방어 처리가 되야합니다.
        @NotBlank(message = "메인 메뉴의 이름을 확인할 수 없습니다. 문제가 지속되면 관리자에게 문의 하십시오.")
        private String mainMenuNm;
        @NotBlank(message = "메인 메뉴 경로를 확인할 수 없습니다. 문제가 지속되면 관리자에게 문의 하십시오.")
        private String mainMenuPath;
        @NotNull(message = "메인 메뉴 소트를 확인할 수 없습니다.")
        private Integer mainMenuSort;
    }
}
