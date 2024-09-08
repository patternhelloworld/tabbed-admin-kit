package com.autofocus.pms.security.oauth2.config.security.response.common.error.auth;


import org.springframework.security.core.AuthenticationException;

public class OtpValueUnauthorizedException extends AuthenticationException {
    public OtpValueUnauthorizedException(String message) {
        super(message);
    }
}