package com.autofocus.pms.security.oauth2.config.security.converter.auth.endpoint;

import com.autofocus.pms.security.oauth2.config.security.token.CustomGrantAuthenticationToken;
import com.autofocus.pms.security.oauth2.config.security.util.RequestOAuth2Distiller;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.Map;

@RequiredArgsConstructor
public class CustomGrantAuthenticationConverter implements AuthenticationConverter {
    /*
    *   `
    *      CustomGrantAuthenticationToken <- OAuth2ClientAuthenticationToken
    *
    * */
    @Override
    public Authentication convert(HttpServletRequest request) {

        OAuth2ClientAuthenticationToken oAuth2ClientAuthenticationToken = (OAuth2ClientAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> additionalParameters = RequestOAuth2Distiller.getTokenUsingSecurityAdditionalParameters(request);

        return new CustomGrantAuthenticationToken(new AuthorizationGrantType((String) additionalParameters.get("grant_type")),
                oAuth2ClientAuthenticationToken, additionalParameters);
    }

}
