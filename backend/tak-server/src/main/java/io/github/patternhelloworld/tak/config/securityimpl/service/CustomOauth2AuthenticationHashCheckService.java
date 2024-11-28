package io.github.patternhelloworld.tak.config.securityimpl.service;

import io.github.patternhelloworld.tak.config.securityimpl.message.CustomSecurityUserExceptionMessage;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.user.entity.Password;

import io.github.patternknife.securityhelper.oauth2.api.config.security.message.ISecurityUserExceptionMessageService;

import io.github.patternknife.securityhelper.oauth2.api.config.security.response.error.dto.KnifeErrorMessages;
import io.github.patternknife.securityhelper.oauth2.api.config.security.response.error.exception.KnifeOauth2AuthenticationException;
import io.github.patternknife.securityhelper.oauth2.api.config.security.serivce.IOauth2AuthenticationHashCheckService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomOauth2AuthenticationHashCheckService implements IOauth2AuthenticationHashCheckService {

    private final PasswordEncoder passwordEncoder;
    private final ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService;


    public void validateUsernamePassword(String inputPassword, @Nullable UserDetails userDetails){
        if (userDetails == null) {
            throw new io.github.patternknife.securityhelper.oauth2.api.config.security.response.error.exception.KnifeOauth2AuthenticationException(iSecurityUserExceptionMessageService.getUserMessage(CustomSecurityUserExceptionMessage.AUTHENTICATION_ID_NO_EXISTS));
        }

        if (!passwordEncoder.matches(inputPassword, userDetails.getPassword())) {
            if(((AccessTokenUserInfo)userDetails).getAdditionalAccessTokenUserInfo().getInfo().getPasswordFailedCount() >= Password.FAILED_LIMIT - 1){
                throw new KnifeOauth2AuthenticationException(KnifeErrorMessages.builder()
                        .userDetails(userDetails)
                        .userMessage(CustomSecurityUserExceptionMessage
                                .AUTHENTICATION_PASSWORD_FAILED_EXCEEDED.getMessage()).message(CustomSecurityUserExceptionMessage.AUTHENTICATION_WRONG_ID_PASSWORD.getMessage() + " (inputPassword : " + inputPassword + ", input username : " + userDetails.getUsername() + ")").build());
            }
            throw new KnifeOauth2AuthenticationException(KnifeErrorMessages.builder()
                    .userDetails(userDetails)
                    .userMessage(iSecurityUserExceptionMessageService.getUserMessage(CustomSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_FAILURE)).message(CustomSecurityUserExceptionMessage.AUTHENTICATION_WRONG_ID_PASSWORD.getMessage() + " (inputPassword : " + inputPassword + ", input username : " + userDetails.getUsername() + ")").build());
        }
    }

    public void validateClientCredentials(String inputClientSecret, RegisteredClient registeredClient){
        if (registeredClient == null) {
            throw new KnifeOauth2AuthenticationException(iSecurityUserExceptionMessageService.getUserMessage(CustomSecurityUserExceptionMessage.AUTHENTICATION_WRONG_CLIENT_ID_SECRET));
        }
        if (!passwordEncoder.matches(inputClientSecret, registeredClient.getClientSecret())) {
            throw new KnifeOauth2AuthenticationException(KnifeErrorMessages.builder()
                    .userMessage(iSecurityUserExceptionMessageService.getUserMessage(CustomSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_FAILURE)).message(iSecurityUserExceptionMessageService.getUserMessage(CustomSecurityUserExceptionMessage.AUTHENTICATION_WRONG_CLIENT_ID_SECRET) + " (inputClientSecret : " + inputClientSecret+ ")").build());
        }
    }

}
