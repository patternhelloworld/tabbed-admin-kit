package com.autofocus.pms.security.oauth2.config.security.response.common.error.auth;

import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class UserDeletedException extends UsernameNotFoundException {
    public UserDeletedException(String message) {
        super(message);
    }
}