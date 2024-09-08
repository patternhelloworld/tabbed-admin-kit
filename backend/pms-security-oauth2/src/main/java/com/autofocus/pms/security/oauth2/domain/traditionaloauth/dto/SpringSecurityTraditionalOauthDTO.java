package com.autofocus.pms.security.oauth2.domain.traditionaloauth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class SpringSecurityTraditionalOauthDTO {

    @Getter
    @Setter
    public static class TokenRequest {

        private String username;
        private String password;

        private String refresh_token;

        @NotBlank
        private String grant_type;

        private String otp_value;

    }


    @AllArgsConstructor
    @Getter
    public static class TokenResponse {
        private String access_token;
        private String token_type = "Bearer";
        private String refresh_token;
        private int expires_in;
        private String scope;
    }


}
