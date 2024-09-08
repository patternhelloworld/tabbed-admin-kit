package com.autofocus.pms.security.oauth2.config.security.serivce.persistence.client;


import com.autofocus.pms.common.config.response.error.dto.ErrorMessages;
import com.autofocus.pms.security.oauth2.config.security.entity.CustomOauthClientDetail;
import com.autofocus.pms.security.oauth2.config.security.response.common.error.auth.CustomOauth2AuthenticationException;
import com.autofocus.pms.security.oauth2.config.security.message.DefaultSecurityUserExceptionMessage;
import com.autofocus.pms.security.oauth2.config.security.dao.OauthClientDetailRepository;
import com.autofocus.pms.security.oauth2.config.security.message.ISecurityUserExceptionMessageService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RegisteredClientRepositoryImpl implements RegisteredClientRepository {

    private Map<String, @NotNull RegisteredClient> cachedRegisteredClientsByClientId = new HashMap<>();

    private final OauthClientDetailRepository oauthClientDetailRepository;

    private final ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService;

    @Override
    public void save(RegisteredClient registeredClient) {

        CustomOauthClientDetail detail = new CustomOauthClientDetail();
        detail.setClientId(registeredClient.getClientId());
        detail.setClientSecret(registeredClient.getClientSecret());
        detail.setScope(String.join(",", registeredClient.getScopes()));

        String grantTypes = registeredClient.getAuthorizationGrantTypes().stream()
                .map(AuthorizationGrantType::getValue)
                .collect(Collectors.joining(","));
        detail.setAuthorizedGrantTypes(grantTypes);


        detail.setAccessTokenValidity(registeredClient.getTokenSettings().getAccessTokenTimeToLive().getSeconds());
        detail.setRefreshTokenValidity(registeredClient.getTokenSettings().getRefreshTokenTimeToLive().getSeconds());

        oauthClientDetailRepository.save(detail);

        // Cache the registered client as long as the persistence logic above is successful.
        cachedRegisteredClientsByClientId.put(registeredClient.getClientId(), registeredClient);
    }

    @Override
    public @NotNull RegisteredClient findById(String id) throws CustomOauth2AuthenticationException {
        return oauthClientDetailRepository.findById(id)
                .map(this::mapToRegisteredClient)
                .orElseThrow(()->
                        new CustomOauth2AuthenticationException(ErrorMessages.builder().message("Couldn't find the ID : " + id)
                                .userMessage(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_WRONG_CLIENT_ID_SECRET)).build()));
    }
    @Override
    public @NotNull RegisteredClient findByClientId(String clientId) throws CustomOauth2AuthenticationException {

        try {
            // Check if the client is in the cache
            RegisteredClient cachedClient = cachedRegisteredClientsByClientId.get(clientId);
            if (cachedClient != null) {
                return cachedClient;
            }

            // If not in cache, refresh the cache
            cache();
            cachedClient = cachedRegisteredClientsByClientId.get(clientId);
            if (cachedClient != null) {
                return cachedClient;
            }
        } catch (Exception e) {
            flush();
        }


        return oauthClientDetailRepository.findById(clientId)
                .map(this::mapToRegisteredClient)
                .orElseThrow(()->
                        new CustomOauth2AuthenticationException(ErrorMessages.builder().message("Couldn't find the client ID : " + clientId)
                                .userMessage(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_WRONG_CLIENT_ID_SECRET)).build()));


    }


    private RegisteredClient mapToRegisteredClient(CustomOauthClientDetail detail) {
        Set<String> scopesSet = Arrays.stream(detail.getScope().split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        Set<AuthorizationGrantType> grantTypesSet = Arrays.stream(detail.getAuthorizedGrantTypes().split(","))
                .map(String::trim)
                .map(AuthorizationGrantType::new)
                .collect(Collectors.toSet());

        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(detail.getClientId())
                .clientSecret(detail.getClientSecret())
                .clientName(detail.getClientId()) // Adjust according to your needs.
                .clientAuthenticationMethods(authenticationMethods ->
                        authenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)) // Adjust based on your entity
                .authorizationGrantTypes(grantTypes -> grantTypes.addAll(grantTypesSet))
                .scopes(scopes -> scopes.addAll(scopesSet))
                .redirectUri("")
                // Add additional configurations as needed, e.g., redirectUris
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .accessTokenTimeToLive(Duration.ofSeconds(detail.getAccessTokenValidity()))
                        .refreshTokenTimeToLive(Duration.ofSeconds(detail.getRefreshTokenValidity()))
                        .build())
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build()) // Adjust accordingly
                .build();
    }

    public void cache() {
        List<RegisteredClient> allClients = oauthClientDetailRepository.findAll().stream()
                .map(this::mapToRegisteredClient)
                .toList();
        // Cache all registered clients
        for (RegisteredClient client : allClients) {
            cachedRegisteredClientsByClientId.put(client.getClientId(), client);
        }
    }

    public void flush() {
        cachedRegisteredClientsByClientId = new HashMap<>();
    }

}
