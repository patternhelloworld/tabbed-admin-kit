package com.autofocus.pms.security.oauth2.domain.traditionaloauth.service;

import com.autofocus.pms.common.config.response.error.dto.ErrorMessages;
import com.autofocus.pms.security.oauth2.config.logger.CustomSecurityLogConfig;
import com.autofocus.pms.security.oauth2.config.security.response.common.error.auth.CustomOauth2AuthenticationException;
import com.autofocus.pms.security.oauth2.config.security.message.DefaultSecurityUserExceptionMessage;
import com.autofocus.pms.security.oauth2.config.security.message.ISecurityUserExceptionMessageService;
import com.autofocus.pms.security.oauth2.config.security.serivce.CommonOAuth2AuthorizationCycle;
import com.autofocus.pms.security.oauth2.config.security.serivce.IOauth2AuthenticationHashCheckService;
import com.autofocus.pms.security.oauth2.config.security.serivce.persistence.authorization.OAuth2AuthorizationServiceImpl;
import com.autofocus.pms.security.oauth2.config.security.serivce.persistence.client.RegisteredClientRepositoryImpl;
import com.autofocus.pms.security.oauth2.config.security.serivce.userdetail.ConditionalDetailsService;
import com.autofocus.pms.security.oauth2.config.security.util.RequestOAuth2Distiller;
import com.autofocus.pms.security.oauth2.domain.traditionaloauth.bo.BasicTokenResolver;
import com.autofocus.pms.security.oauth2.domain.traditionaloauth.dto.SpringSecurityTraditionalOauthDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
public class TraditionalOauthService {

    private static final Logger logger = LoggerFactory.getLogger(CustomSecurityLogConfig.class);

    private final RegisteredClientRepositoryImpl registeredClientRepository;

    private final OAuth2AuthorizationServiceImpl authorizationService;

    private final ConditionalDetailsService conditionalDetailsService;

    private final CommonOAuth2AuthorizationCycle commonOAuth2AuthorizationCycle;
    private final IOauth2AuthenticationHashCheckService iOauth2AuthenticationHashCheckService;

    private final ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService;

    public TraditionalOauthService(RegisteredClientRepositoryImpl registeredClientRepository,
                                   OAuth2AuthorizationServiceImpl authorizationService,
                                   ConditionalDetailsService conditionalDetailsService,
                                   CommonOAuth2AuthorizationCycle commonOAuth2AuthorizationCycle,
                                   IOauth2AuthenticationHashCheckService iOauth2AuthenticationHashCheckService,
                                   ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService) {

        this.registeredClientRepository = registeredClientRepository;
        this.authorizationService = authorizationService;
        this.conditionalDetailsService = conditionalDetailsService;

        this.commonOAuth2AuthorizationCycle = commonOAuth2AuthorizationCycle;
        this.iOauth2AuthenticationHashCheckService = iOauth2AuthenticationHashCheckService;

        this.iSecurityUserExceptionMessageService = iSecurityUserExceptionMessageService;

    }


    public SpringSecurityTraditionalOauthDTO.TokenResponse createAccessToken(SpringSecurityTraditionalOauthDTO.TokenRequest accessTokenRequest,
                                                                             String authorizationHeader) {
        try {
            BasicTokenResolver.BasicCredentials basicCredentials = BasicTokenResolver.parse(authorizationHeader).orElseThrow(() -> new CustomOauth2AuthenticationException(ErrorMessages.builder().message("Header parsing error (header : " + authorizationHeader).userMessage(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_WRONG_CLIENT_ID_SECRET)).build()));

            RegisteredClient registeredClient = registeredClientRepository.findByClientId(basicCredentials.getClientId());

            iOauth2AuthenticationHashCheckService.validateClientCredentials(basicCredentials.getClientSecret(), registeredClient);

            UserDetails userDetails = conditionalDetailsService.loadUserByUsername(accessTokenRequest.getUsername(), basicCredentials.getClientId());

            iOauth2AuthenticationHashCheckService.validateUsernamePassword(accessTokenRequest.getPassword(), userDetails);

            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            OAuth2Authorization oAuth2Authorization = commonOAuth2AuthorizationCycle.run(userDetails,
                    new AuthorizationGrantType(accessTokenRequest.getGrant_type()), basicCredentials.getClientId(), RequestOAuth2Distiller.getTokenUsingSecurityAdditionalParameters(request), null);

            Instant now = Instant.now();
            Instant expiresAt = oAuth2Authorization.getAccessToken().getToken().getExpiresAt();
            int accessTokenRemainingSeconds = Math.toIntExact(Duration.between(now, expiresAt).getSeconds());

            return new SpringSecurityTraditionalOauthDTO.TokenResponse(
                    oAuth2Authorization.getAccessToken().getToken().getTokenValue(), OAuth2AccessToken.TokenType.BEARER.getValue(), Objects.requireNonNull(oAuth2Authorization.getRefreshToken()).getToken().getTokenValue(),
                    accessTokenRemainingSeconds,
                    String.join(" ", registeredClient.getScopes()));

        } catch (UsernameNotFoundException e) {
            throw new CustomOauth2AuthenticationException(e.getMessage());
        } catch (CustomOauth2AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomOauth2AuthenticationException(e.getMessage() + "///" + e.getStackTrace().toString());
        }
    }

    public SpringSecurityTraditionalOauthDTO.TokenResponse refreshAccessToken(SpringSecurityTraditionalOauthDTO.TokenRequest refreshTokenRequest,
                                                                              String authorizationHeader) {
        try {
            BasicTokenResolver.BasicCredentials basicCredentials = BasicTokenResolver.parse(authorizationHeader).orElseThrow(() -> new CustomOauth2AuthenticationException(ErrorMessages.builder().message("Header parsing error (header : " + authorizationHeader).userMessage(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_WRONG_CLIENT_ID_SECRET)).build()));

            RegisteredClient registeredClient = registeredClientRepository.findByClientId(basicCredentials.getClientId());

            OAuth2Authorization oAuth2Authorization = authorizationService.findByToken(refreshTokenRequest.getRefresh_token(), OAuth2TokenType.REFRESH_TOKEN);

            UserDetails userDetails;
            if (oAuth2Authorization == null || oAuth2Authorization.getRefreshToken() == null) {
                throw new CustomOauth2AuthenticationException(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_ERROR));
            } else {
                userDetails = conditionalDetailsService.loadUserByUsername(oAuth2Authorization.getPrincipalName(), registeredClient.getClientId());
            }

            Map<String, Object> modifiableAdditionalParameters = new HashMap<>();
            modifiableAdditionalParameters.put("refresh_token", refreshTokenRequest.getRefresh_token());

            oAuth2Authorization = commonOAuth2AuthorizationCycle.run(userDetails,
                    new AuthorizationGrantType(refreshTokenRequest.getGrant_type()),
                    basicCredentials.getClientId(), oAuth2Authorization.getAttributes(), modifiableAdditionalParameters);


            Instant now = Instant.now();
            Instant expiresAt = oAuth2Authorization.getRefreshToken().getToken().getExpiresAt();
            int refreshTokenRemainingSeconds = Math.toIntExact(Duration.between(now, expiresAt).getSeconds());

            return new SpringSecurityTraditionalOauthDTO.TokenResponse(
                    oAuth2Authorization.getAccessToken().getToken().getTokenValue(), OAuth2AccessToken.TokenType.BEARER.getValue(), Objects.requireNonNull(oAuth2Authorization.getRefreshToken()).getToken().getTokenValue(),
                    refreshTokenRemainingSeconds,
                    String.join(" ", registeredClient.getScopes()));

        }catch (UsernameNotFoundException e){
            throw new CustomOauth2AuthenticationException(e.getMessage());
        }catch (CustomOauth2AuthenticationException e){
            throw e;
        }  catch (Exception e){
            throw new CustomOauth2AuthenticationException(ErrorMessages.builder().message(e.getMessage()).userMessage(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_ERROR)).build());
        }
    }

}
