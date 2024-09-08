package com.autofocus.pms.security.oauth2.config.security.response.common.error.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserRestoredException extends AccessDeniedException {
    public UserRestoredException(String message) {
        super(message);
    }
}