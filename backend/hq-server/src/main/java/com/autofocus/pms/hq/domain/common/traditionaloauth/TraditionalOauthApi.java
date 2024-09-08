package com.autofocus.pms.hq.domain.common.traditionaloauth;


import com.autofocus.pms.common.config.response.GlobalSuccessPayload;
import com.autofocus.pms.common.config.response.error.dto.ErrorMessages;
import com.autofocus.pms.hq.domain.common.user.service.UserService;
import com.autofocus.pms.security.oauth2.config.security.message.DefaultSecurityUserExceptionMessage;
import com.autofocus.pms.security.oauth2.config.security.message.ISecurityUserExceptionMessageService;
import com.autofocus.pms.security.oauth2.config.security.response.common.error.auth.CustomOauth2AuthenticationException;
import com.autofocus.pms.security.oauth2.domain.traditionaloauth.dto.SpringSecurityTraditionalOauthDTO;
import com.autofocus.pms.security.oauth2.domain.traditionaloauth.service.TraditionalOauthService;
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
                throw new CustomOauth2AuthenticationException(ErrorMessages.builder().message("Wrong grant type from Req : " + tokenRequest.getGrant_type()).userMessage(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_WRONG_GRANT_TYPE)).build());
        }
    }

}
