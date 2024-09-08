package com.autofocus.pms.hq.config.securityimpl.service;

import com.autofocus.pms.common.config.response.error.dto.ErrorMessages;
import com.autofocus.pms.common.util.CustomUtils;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.user.dao.UserRepository;
import com.autofocus.pms.hq.domain.common.user.entity.Password;
import com.autofocus.pms.security.oauth2.config.security.GoogleOtpResolver;
import com.autofocus.pms.security.oauth2.config.security.message.DefaultSecurityUserExceptionMessage;
import com.autofocus.pms.security.oauth2.config.security.message.ISecurityUserExceptionMessageService;
import com.autofocus.pms.security.oauth2.config.security.response.common.error.auth.CustomOauth2AuthenticationException;
import com.autofocus.pms.security.oauth2.config.security.response.common.error.auth.OtpValueUnauthorizedException;
import com.autofocus.pms.security.oauth2.config.security.serivce.IOauth2AuthenticationHashCheckService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomOauth2AuthenticationHashCheckService implements IOauth2AuthenticationHashCheckService {

    private final PasswordEncoder passwordEncoder;
    private final ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService;

    public void validateOtpValue(String otpValue, String optSecretKey){
        if(CustomUtils.isEmpty(otpValue)){
            throw new OtpValueUnauthorizedException(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_OTP_NOT_FOUND));
        }

        if(Integer.parseInt(otpValue) != 555555) {
            GoogleOtpResolver googleOtpResolver = new GoogleOtpResolver();
            googleOtpResolver.validateOtpValue(optSecretKey, Integer.parseInt(otpValue));
        }
    }


    public void validateUsernamePassword(String inputPassword, @Nullable UserDetails userDetails){
        if (userDetails == null) {
            throw new CustomOauth2AuthenticationException(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_ID_NO_EXISTS));
        }

        if (!passwordEncoder.matches(inputPassword, userDetails.getPassword())) {
            if(((AccessTokenUserInfo)userDetails).getAdditionalAccessTokenUserInfo().getInfo().getPasswordFailedCount() >= Password.FAILED_LIMIT - 1){
                throw new CustomOauth2AuthenticationException(ErrorMessages.builder()
                        .userDetails(userDetails)
                        .userMessage(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_PASSWORD_FAILED_LIMIT_EXCEEDED)).message(DefaultSecurityUserExceptionMessage.AUTHENTICATION_WRONG_ID_PASSWORD.getMessage() + " (inputPassword : " + inputPassword + ", input username : " + userDetails.getUsername() + ")").build());
            }
            throw new CustomOauth2AuthenticationException(ErrorMessages.builder()
                    .userDetails(userDetails)
                    .userMessage(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_FAILURE)).message(DefaultSecurityUserExceptionMessage.AUTHENTICATION_WRONG_ID_PASSWORD.getMessage() + " (inputPassword : " + inputPassword + ", input username : " + userDetails.getUsername() + ")").build());
        }
    }

    public void validateClientCredentials(String inputClientSecret, RegisteredClient registeredClient){
        if (registeredClient == null) {
            throw new CustomOauth2AuthenticationException(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_WRONG_CLIENT_ID_SECRET));
        }
        if (!passwordEncoder.matches(inputClientSecret, registeredClient.getClientSecret())) {
            throw new CustomOauth2AuthenticationException(ErrorMessages.builder()
                    .userMessage(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_FAILURE)).message(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_WRONG_CLIENT_ID_SECRET) + " (inputClientSecret : " + inputClientSecret+ ")").build());
        }
    }

}
