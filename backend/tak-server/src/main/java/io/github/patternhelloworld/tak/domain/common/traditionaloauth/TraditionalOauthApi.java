package io.github.patternhelloworld.tak.domain.common.traditionaloauth;


import io.github.patternhelloworld.common.config.response.GlobalSuccessPayload;

import io.github.patternhelloworld.tak.config.securityimpl.message.CustomSecurityUserExceptionMessage;
import io.github.patternhelloworld.tak.domain.common.user.service.UserService;

import io.github.patternknife.securityhelper.oauth2.api.config.security.message.ISecurityUserExceptionMessageService;

import io.github.patternknife.securityhelper.oauth2.api.config.security.response.error.dto.KnifeErrorMessages;

import io.github.patternknife.securityhelper.oauth2.api.domain.traditionaloauth.dto.SpringSecurityTraditionalOauthDTO;
import io.github.patternknife.securityhelper.oauth2.api.domain.traditionaloauth.service.TraditionalOauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TraditionalOauthApi {

    private final TraditionalOauthService traditionalOauthService;
    private final ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService;
    private final UserService userService;

    @PostMapping("/traditional-oauth/token")
    public GlobalSuccessPayload<SpringSecurityTraditionalOauthDTO.TokenResponse> createAccessToken(
            @ModelAttribute SpringSecurityTraditionalOauthDTO.TokenRequest tokenRequest,
            @RequestHeader("Authorization") String authorizationHeader) {
        switch(tokenRequest.getGrant_type()) {
            case "password":
                SpringSecurityTraditionalOauthDTO.TokenResponse tokenResponse = traditionalOauthService.createAccessToken(tokenRequest, authorizationHeader);
                userService.resetPasswordFailedCountByAccessToken(tokenResponse.getAccess_token());

                return new GlobalSuccessPayload<>(tokenResponse);
            case "refresh_token":
                return new GlobalSuccessPayload<>(traditionalOauthService.refreshAccessToken(tokenRequest, authorizationHeader));
            default:
                throw new io.github.patternknife.securityhelper.oauth2.api.config.security.response.error.exception.KnifeOauth2AuthenticationException(KnifeErrorMessages.builder().message("Wrong grant type from Req : " + tokenRequest.getGrant_type()).userMessage(iSecurityUserExceptionMessageService.getUserMessage(CustomSecurityUserExceptionMessage.AUTHENTICATION_WRONG_GRANT_TYPE)).build());
        }
    }

}
