package com.autofocus.pms.security.oauth2.config.security.response.auth.authentication;


import com.autofocus.pms.security.oauth2.config.security.response.common.error.auth.CustomOauth2AuthenticationException;
import com.autofocus.pms.security.oauth2.config.security.message.DefaultSecurityUserExceptionMessage;
import com.autofocus.pms.security.oauth2.config.security.message.ISecurityUserExceptionMessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@RequiredArgsConstructor
public class DefaultAuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter =
            new OAuth2AccessTokenResponseHttpMessageConverter();

    private final ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService;


    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {


        final OAuth2AccessTokenAuthenticationToken accessTokenAuthentication=(OAuth2AccessTokenAuthenticationToken)authentication;

        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();
        /*        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        // Lookup the authorization using the access token
        OAuth2Authorization authorization = this.authorizationService.findByToken(
                accessToken.getTokenValue(), OAuth2TokenType.ACCESS_TOKEN);

        Map<String, Object> opaqueTokenClaims = authorization.getAccessToken().getClaims();
        Authentication userPrincipal = authorization.getAttribute(Principal.class.getName());*/

        OAuth2AccessTokenResponse.Builder builder =
                OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                        .tokenType(accessToken.getTokenType())
                        .scopes(accessToken.getScopes());
        if(((String)additionalParameters.get("grant_type")).equals(AuthorizationGrantType.PASSWORD.getValue())){
            if(accessToken.getExpiresAt() != null) {
                builder.expiresIn(ChronoUnit.SECONDS.between(Instant.now(), accessToken.getExpiresAt()));
            }
        }else if(((String)additionalParameters.get("grant_type")).equals(AuthorizationGrantType.REFRESH_TOKEN.getValue())){
            assert refreshToken != null;
            if(refreshToken.getExpiresAt() != null) {
                builder.expiresIn(ChronoUnit.SECONDS.between(Instant.now(), refreshToken.getExpiresAt()));
            }
        }else{
            throw new CustomOauth2AuthenticationException(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_WRONG_GRANT_TYPE));
        }


        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }
/*        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }*/

        // TODO Add custom response parameters using `opaqueTokenClaims` and/or `userPrincipal`


        OAuth2AccessTokenResponse accessTokenResponse = builder.build();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
    }
}
