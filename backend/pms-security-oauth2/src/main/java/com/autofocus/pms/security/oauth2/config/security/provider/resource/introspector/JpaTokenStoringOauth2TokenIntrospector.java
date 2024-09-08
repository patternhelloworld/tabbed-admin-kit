package com.autofocus.pms.security.oauth2.config.security.provider.resource.introspector;


import com.autofocus.pms.security.oauth2.config.security.response.common.error.auth.CustomOauth2AuthenticationException;
import com.autofocus.pms.security.oauth2.config.security.message.DefaultSecurityUserExceptionMessage;
import com.autofocus.pms.security.oauth2.config.security.message.ISecurityUserExceptionMessageService;
import com.autofocus.pms.security.oauth2.config.security.serivce.persistence.authorization.OAuth2AuthorizationServiceImpl;
import com.autofocus.pms.security.oauth2.config.security.serivce.userdetail.ConditionalDetailsService;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;


public class JpaTokenStoringOauth2TokenIntrospector implements OpaqueTokenIntrospector {


    private OpaqueTokenIntrospector delegate =
            new NimbusOpaqueTokenIntrospector(
                    "http://localhost:8300/oauth2/introspect",
                    "barClient",
                    "barClientSecret"
            );

    private final OAuth2AuthorizationServiceImpl authorizationService;
    private final ConditionalDetailsService conditionalDetailsService;
    private final ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService;

    public JpaTokenStoringOauth2TokenIntrospector(OAuth2AuthorizationServiceImpl authorizationService,
                                                  ConditionalDetailsService conditionalDetailsService,
                                                  ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService) {
        this.authorizationService = authorizationService;
        this.conditionalDetailsService = conditionalDetailsService;
        this.iSecurityUserExceptionMessageService = iSecurityUserExceptionMessageService;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {

/*        try {
            OAuth2AuthenticatedPrincipal principal = delegate.introspect(token);
            return principal;
        }catch (Exception e){
            //throw e;
            throw new CustomOauth2AuthenticationException(e.getMessage());
        }*/

        OAuth2Authorization oAuth2Authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

        if(oAuth2Authorization == null || oAuth2Authorization.getAccessToken() == null || oAuth2Authorization.getAccessToken().isExpired()
                || oAuth2Authorization.getRefreshToken() == null || oAuth2Authorization.getRefreshToken().isExpired()){
            throw new CustomOauth2AuthenticationException(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_TOKEN_FAILURE));
            //return null;
        }

        return (OAuth2AuthenticatedPrincipal) conditionalDetailsService.loadUserByUsername(oAuth2Authorization.getPrincipalName(), (String) oAuth2Authorization.getAttributes().get("client_id"));
    }
}