package com.autofocus.pms.security.oauth2.config.security.response.common.error.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class SocialEmailNotProvidedException extends RuntimeException {
    public SocialEmailNotProvidedException(String message) {
        super(message);
    }
}