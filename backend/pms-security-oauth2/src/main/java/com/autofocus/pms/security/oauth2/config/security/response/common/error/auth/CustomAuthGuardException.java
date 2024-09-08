package com.autofocus.pms.security.oauth2.config.security.response.common.error.auth;

import org.springframework.security.access.AccessDeniedException;

public class CustomAuthGuardException extends AccessDeniedException {

    public CustomAuthGuardException(String message) {
        super(message);
    }
}