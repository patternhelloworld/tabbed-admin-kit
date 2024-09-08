package com.autofocus.pms.security.oauth2.config.security.response.common.error.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 인증 (UNAUTHORIZED) : 401
// 승인 (FORBIDDEN) : 403
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class SocialUnauthorizedException extends RuntimeException {
    public SocialUnauthorizedException(String message) {
        super(message);
    }
}