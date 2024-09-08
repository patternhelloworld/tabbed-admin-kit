package com.autofocus.pms.security.oauth2.config.security.serivce.authentication;

import com.autofocus.pms.security.oauth2.config.security.token.CustomGrantAuthenticationToken;
import com.autofocus.pms.security.oauth2.config.security.token.generator.CustomAccessTokenCustomizer;
import com.autofocus.pms.security.oauth2.config.security.token.generator.CustomDelegatingOAuth2TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2AuthorizationBuildingServiceImpl implements OAuth2AuthorizationBuildingService {

    private final RegisteredClientRepository registeredClientRepository;
    private final CustomDelegatingOAuth2TokenGenerator customTokenGenerator;


    private OAuth2Authorization build(String clientId, UserDetails userDetails,
                                      CustomGrantAuthenticationToken customGrantAuthenticationToken,
                                        OAuth2RefreshToken shouldBePreservedRefreshToken) {

        RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);

        if(AuthorizationServerContextHolder.getContext() == null){

            AuthorizationServerContext authorizationServerContext = new AuthorizationServerContext() {
                @Override
                public String getIssuer() {
                    return null;
                }

                @Override
                public AuthorizationServerSettings getAuthorizationServerSettings() {
                    return null;
                }
            };
            AuthorizationServerContextHolder.setContext(authorizationServerContext);
        }


        customTokenGenerator.setCustomizer(
                CustomDelegatingOAuth2TokenGenerator.GeneratorType.ACCESS_TOKEN,
                new CustomAccessTokenCustomizer(userDetails)
        );


        OAuth2Token accessToken = customTokenGenerator.generate(DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(customGrantAuthenticationToken)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .authorizationGrantType(customGrantAuthenticationToken.getGrantType())
                .authorizationGrant(customGrantAuthenticationToken)
                .authorizedScopes(registeredClient.getScopes())
                .build());


        OAuth2Token refreshToken = shouldBePreservedRefreshToken != null ? shouldBePreservedRefreshToken : customTokenGenerator.generate(DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .principal(customGrantAuthenticationToken)
                .tokenType(OAuth2TokenType.REFRESH_TOKEN)
                .authorizationGrantType(customGrantAuthenticationToken.getGrantType())
                .authorizationGrant(customGrantAuthenticationToken)
                .authorizedScopes(registeredClient.getScopes())
                .build());


        return OAuth2Authorization
                .withRegisteredClient(registeredClient)
                .principalName(userDetails.getUsername())
                .authorizationGrantType(customGrantAuthenticationToken.getGrantType())
                .attribute("authorities", customGrantAuthenticationToken.getAuthorities())
                .attributes(attrs -> attrs.putAll(customGrantAuthenticationToken.getAdditionalParameters()))
                .accessToken(new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        accessToken.getTokenValue(),
                        accessToken.getIssuedAt(),
                        accessToken.getExpiresAt(),
                        registeredClient.getScopes()
                ))
                .refreshToken(new OAuth2RefreshToken(
                        refreshToken.getTokenValue(),
                        refreshToken.getIssuedAt(),
                        refreshToken.getExpiresAt()
                ))
                .build();
    }

    @Override
    public OAuth2Authorization build(UserDetails userDetails, AuthorizationGrantType grantType,String clientId,
                                     Map<String, Object> additionalParameters, OAuth2RefreshToken shouldBePreservedRefreshToken) {

        CustomGrantAuthenticationToken customGrantAuthenticationToken =
                new CustomGrantAuthenticationToken(grantType, userDetails, additionalParameters);

        return build(clientId, userDetails, customGrantAuthenticationToken, shouldBePreservedRefreshToken);
    }

}
