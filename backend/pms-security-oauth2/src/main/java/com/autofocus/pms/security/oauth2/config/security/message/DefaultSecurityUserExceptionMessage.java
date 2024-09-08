package com.autofocus.pms.security.oauth2.config.security.message;

import com.autofocus.pms.common.config.response.error.message.ExceptionMessageInterface;

public enum DefaultSecurityUserExceptionMessage implements ExceptionMessageInterface {

    AUTHENTICATION_LOGIN_FAILURE("인증 정보가 유효하지 않습니다. 다시 확인하고 시도해 주세요."),
    AUTHENTICATION_LOGIN_ERROR("인증 상의 오류가 발생하였습니다. 문제가 지속되면 고객센터에 문의 바랍니다."),
    AUTHENTICATION_TOKEN_FAILURE("인증 토큰이 만료 되었습니다. 다시 로그인을 진행해주세요."),
    AUTHENTICATION_TOKEN_NOT_FOUND("인증 토큰을 확인할 수 없습니다. 다시 로그인을 진행해주세요."),
    AUTHENTICATION_TOKEN_ERROR("인증 토큰 확인에 문제가 발생 하였습니다. 다시 로그인을 진행해주세요."),
    AUTHORIZATION_FAILURE("접근 권한이 없습니다. 관리자에게 요청하십시오."),
    AUTHORIZATION_ERROR("접근 권한 상의 오류가 발생하였습니다. 문제가 지속되면 고객센터에 문의 바랍니다."),

    // OTP
    AUTHENTICATION_OTP_NOT_FOUND("OTP 값이 확인되지 않습니다."),

    // ID PASSWORD
    AUTHENTICATION_ID_NO_EXISTS("해당 아이디가 존재하지 않습니다."),
    AUTHENTICATION_WRONG_ID_PASSWORD("사용자 정보가 확인 되지 않습니다. ID 또는 비밀번호를 확인하십시오. 문제가 지속된다면 고객센터에 문의주십시오."),
    AUTHENTICATION_PASSWORD_FAILED_LIMIT_EXCEEDED("비밀번호 입력 횟수를 초과하였습니다."),

    // CLIENT ID, SECRET
    AUTHENTICATION_WRONG_CLIENT_ID_SECRET("클라이언트 ID 또는 Secret 이 잘못 되었습니다."),

    // GRANT TYPE
    AUTHENTICATION_WRONG_GRANT_TYPE("유효한 grant_type 이 아닙니다.");

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    DefaultSecurityUserExceptionMessage(String message) {
        this.message = message;
    }

}
