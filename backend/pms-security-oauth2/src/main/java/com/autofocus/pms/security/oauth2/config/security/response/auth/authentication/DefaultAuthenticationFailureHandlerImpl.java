package com.autofocus.pms.security.oauth2.config.security.response.auth.authentication;


import com.autofocus.pms.common.config.response.error.CustomExceptionUtils;
import com.autofocus.pms.common.config.response.error.dto.ErrorResponsePayload;
import com.autofocus.pms.security.oauth2.config.logger.CustomSecurityLogConfig;
import com.autofocus.pms.security.oauth2.config.security.response.common.error.auth.CustomOauth2AuthenticationException;
import com.autofocus.pms.security.oauth2.config.security.message.DefaultSecurityUserExceptionMessage;
import com.autofocus.pms.security.oauth2.config.security.message.ISecurityUserExceptionMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;


@RequiredArgsConstructor
public class DefaultAuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomSecurityLogConfig.class);

    private final ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {

        ErrorResponsePayload errorResponsePayload;
        String stackTraces = CustomExceptionUtils.getAllStackTraces(exception);
        if(exception instanceof CustomOauth2AuthenticationException){
            errorResponsePayload = new ErrorResponsePayload(((CustomOauth2AuthenticationException) exception).getErrorMessages().getMessage(),
                    "uri=" + request.getRequestURI(), ((CustomOauth2AuthenticationException) exception).getErrorMessages().getUserMessage(), stackTraces);
        }else if(exception instanceof OAuth2AuthenticationException) {
            errorResponsePayload = new ErrorResponsePayload(
                    ((OAuth2AuthenticationException) exception).getError().getErrorCode() + " / " + ((OAuth2AuthenticationException) exception).getError().getDescription(),
                    "uri=" + request.getRequestURI(),
                   iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_FAILURE),
                    stackTraces);
        }else{
            errorResponsePayload = new ErrorResponsePayload(
                    exception.getMessage(),
                    "uri=" + request.getRequestURI(),
                    iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_ERROR),
                    stackTraces);
        }

        // Set response status
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Write the error details to the response
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponsePayload));

        logger.warn(new String(errorResponsePayload.toString().getBytes(), "UTF-8"));

    }
}
