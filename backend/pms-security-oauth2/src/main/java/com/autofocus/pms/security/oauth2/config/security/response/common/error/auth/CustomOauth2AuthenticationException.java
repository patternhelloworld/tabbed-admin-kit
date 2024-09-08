package com.autofocus.pms.security.oauth2.config.security.response.common.error.auth;

import com.autofocus.pms.common.config.response.error.dto.ErrorMessages;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

/*
*   Only OAuth2AuthenticationException is allowed to be tossed.
* */
public class CustomOauth2AuthenticationException extends OAuth2AuthenticationException {
    protected ErrorMessages errorMessages;

    public CustomOauth2AuthenticationException(){
        super("default");
    }
    public CustomOauth2AuthenticationException(String message){
        super(message);
        errorMessages = ErrorMessages.builder().userMessage(message).message(message).build();
    }

    public CustomOauth2AuthenticationException(ErrorMessages errorMessages){
        super(errorMessages.getMessage() == null ? "default" : errorMessages.getMessage());
        this.errorMessages = errorMessages;
    }
    public ErrorMessages getErrorMessages() {
        return errorMessages;
    }

}