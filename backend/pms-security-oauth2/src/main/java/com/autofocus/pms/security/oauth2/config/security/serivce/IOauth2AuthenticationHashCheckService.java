package com.autofocus.pms.security.oauth2.config.security.serivce;

import jakarta.annotation.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

public interface IOauth2AuthenticationHashCheckService {
    void validateOtpValue(String otpValue, String optSecretKey);
    void validateUsernamePassword(String inputPassword, @Nullable UserDetails userDetails);
    void validateClientCredentials(String inputClientSecret, RegisteredClient registeredClient);
}