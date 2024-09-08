package com.autofocus.pms.security.oauth2.config.security.response.common.error.auth;

public class AlreadySocialRegisteredException extends RuntimeException {
    public AlreadySocialRegisteredException(String message) {
        super(message);
    }
}