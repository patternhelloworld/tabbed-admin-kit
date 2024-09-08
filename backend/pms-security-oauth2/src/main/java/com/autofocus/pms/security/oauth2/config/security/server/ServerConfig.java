package com.autofocus.pms.security.oauth2.config.security.server;


import com.autofocus.pms.security.oauth2.config.security.message.DefaultSecurityMessageServiceImpl;
import com.autofocus.pms.security.oauth2.config.security.message.ISecurityUserExceptionMessageService;
import com.autofocus.pms.security.oauth2.config.security.aop.DefaultSecurityPointCut;
import com.autofocus.pms.security.oauth2.config.security.aop.SecurityPointCut;
import com.autofocus.pms.security.oauth2.config.security.converter.auth.endpoint.CustomGrantAuthenticationConverter;
import com.autofocus.pms.security.oauth2.config.security.response.auth.authentication.DefaultAuthenticationFailureHandlerImpl;
import com.autofocus.pms.security.oauth2.config.security.response.resource.authentication.DefaultAuthenticationEntryPoint;

import com.autofocus.pms.security.oauth2.config.security.provider.auth.endpoint.AuthenticationProviderImpl;
import com.autofocus.pms.security.oauth2.config.security.provider.auth.introspectionendpoint.Oauth2OpaqueTokenAuthenticationProvider;
import com.autofocus.pms.security.oauth2.config.security.provider.resource.introspector.JpaTokenStoringOauth2TokenIntrospector;
import com.autofocus.pms.security.oauth2.config.security.serivce.CommonOAuth2AuthorizationCycle;
import com.autofocus.pms.security.oauth2.config.security.serivce.DefaultOauth2AuthenticationHashCheckService;
import com.autofocus.pms.security.oauth2.config.security.serivce.IOauth2AuthenticationHashCheckService;
import com.autofocus.pms.security.oauth2.config.security.serivce.persistence.authorization.OAuth2AuthorizationServiceImpl;
import com.autofocus.pms.security.oauth2.config.security.serivce.persistence.client.RegisteredClientRepositoryImpl;
import com.autofocus.pms.security.oauth2.config.security.serivce.userdetail.ConditionalDetailsService;
import com.autofocus.pms.security.oauth2.config.security.response.auth.authentication.DefaultAuthenticationSuccessHandlerImpl;
import com.autofocus.pms.security.oauth2.config.security.token.generator.CustomDelegatingOAuth2TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;


@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ServerConfig {

    @Primary
    @Bean
    public OAuth2TokenGenerator<OAuth2Token> tokenGenerator() {
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new CustomDelegatingOAuth2TokenGenerator(
                accessTokenGenerator, refreshTokenGenerator);
    }

    /*
    *  로그인 방식에 따른 진입 여부
    *   : hq-server 의 domain/traditionaloauth 에 있는 /api/v1/traditional-oauth/token 방식을 사용할 경우 여기를 통과하지 않습니다.
    *   : 여기는 /oauth2/token 을 호출하는 방식을 사용할 경우 진입합니다.
    * */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            CommonOAuth2AuthorizationCycle commonOAuth2AuthorizationCycle,
            OAuth2AuthorizationServiceImpl authorizationService,
            ConditionalDetailsService conditionalDetailsService,
            IOauth2AuthenticationHashCheckService defaultOauth2AuthenticationHashCheckService,
            OAuth2TokenGenerator<?> tokenGenerator,
            RegisteredClientRepositoryImpl registeredClientRepository,
            ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService,
            AuthenticationFailureHandler iAuthenticationFailureHandler, AuthenticationSuccessHandler iAuthenticationSuccessHandler) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        http.with(authorizationServerConfigurer, Customizer.withDefaults());

        authorizationServerConfigurer
                // 1. oauth_client_details
                .clientAuthentication(clientAuthentication ->
                        clientAuthentication
                                .errorResponseHandler(iAuthenticationFailureHandler)
                )
                .registeredClientRepository(registeredClientRepository)
                // 2. oauth_access_token & oauth_refresh_token : 로그인 시 발생하는 CRUD 에 대한 정의
                .authorizationService(authorizationService)
                // 3. 토큰 암호화 방식
                .tokenGenerator(tokenGenerator)
                // 4. 로그인 시 다음 Bean 들을 차례로 진입
                .tokenEndpoint(tokenEndpoint ->
                        tokenEndpoint
                                .accessTokenRequestConverter(new CustomGrantAuthenticationConverter())
                                .authenticationProvider(new AuthenticationProviderImpl(
                                        commonOAuth2AuthorizationCycle, conditionalDetailsService, defaultOauth2AuthenticationHashCheckService,
                                        authorizationService, iSecurityUserExceptionMessageService
                                ))
                                .accessTokenResponseHandler(iAuthenticationSuccessHandler)
                                .errorResponseHandler(iAuthenticationFailureHandler)

                // 5. Bearer token 을 검증하는 Bean 을 등록
                     // : 현재는 하기 JpaTokenStoringOauth2TokenIntrospector 에서 token 검증 실패 (Oauth2AuthenticationException throw) 시 그 다음 Oauth2OpaqueTokenAuthenticationProvider 로 접근하는 것을 확인.
                ).tokenIntrospectionEndpoint(tokenIntrospectEndpoint ->
                        tokenIntrospectEndpoint
                                .introspectionRequestConverter(httpServletRequest -> new Oauth2OpaqueTokenAuthenticationProvider(
                                        tokenIntrospector(
                                                authorizationService, conditionalDetailsService, iSecurityUserExceptionMessageService
                                        ),authorizationService, conditionalDetailsService
                                ).convert(httpServletRequest))
                                .authenticationProvider(new Oauth2OpaqueTokenAuthenticationProvider(
                                        tokenIntrospector(
                                                authorizationService, conditionalDetailsService, iSecurityUserExceptionMessageService
                                        ),authorizationService, conditionalDetailsService
                                )).errorResponseHandler(iAuthenticationFailureHandler));

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        http.csrf(AbstractHttpConfigurer::disable).securityMatcher(endpointsMatcher).authorizeHttpRequests(authorize ->
                authorize.anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Primary
    @Bean
    BearerTokenResolver bearerTokenResolver() {
        DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
        bearerTokenResolver.setBearerTokenHeaderName(HttpHeaders.AUTHORIZATION);
        return bearerTokenResolver;
    }

    @Bean
    public OpaqueTokenIntrospector tokenIntrospector(OAuth2AuthorizationServiceImpl authorizationService,
                                                     ConditionalDetailsService conditionalDetailsService, ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService) {
        return new JpaTokenStoringOauth2TokenIntrospector(authorizationService, conditionalDetailsService, iSecurityUserExceptionMessageService);
    }

    /*
    *   로그인 후 얻은 Bearer 토큰을 검증. 예를 들어, hq.domain 에 있는 api 에 REST 접근 시 @PreAuthorize("...") 있는 API 들은 여기의
    *   방식으로 검증
    *     : 현재의 검증 방식은 DB 직접 조회이나, 위 상기 .tokenIntrospectionEndpoint 를 호출하여 토큰을 검증하는 방식도 있다.
    * */
    @Bean
    @Order(2)
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http, OAuth2AuthorizationServiceImpl authorizationService,
                                                                 ConditionalDetailsService conditionalDetailsService,
                                                                 ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService,
                                                                 AuthenticationEntryPoint iAuthenticationEntryPoint
                                                                 ) throws Exception {

        DefaultBearerTokenResolver resolver = new DefaultBearerTokenResolver();
        resolver.setAllowFormEncodedBodyParameter(true);

        http.csrf(AbstractHttpConfigurer::disable)
                        .oauth2ResourceServer(oauth2 -> oauth2
                        .bearerTokenResolver(resolver)
                                .authenticationEntryPoint(iAuthenticationEntryPoint)
                        .opaqueToken(opaqueToken -> opaqueToken.introspector(tokenIntrospector(authorizationService, conditionalDetailsService, iSecurityUserExceptionMessageService))));

        return http.build();
    }


    /*
    *
    *   여기 아래의 모든 Bean 들은 여기 있는 Default 들을 사용하지 않을 경우 직접 구현하여 사용 하십시오.
    *
    * */

    @Bean
    @ConditionalOnMissingBean(SecurityPointCut.class)
    public SecurityPointCut securityPointCut() {
        return new DefaultSecurityPointCut();
    }

    @Bean
    @ConditionalOnMissingBean(ISecurityUserExceptionMessageService.class)
    public ISecurityUserExceptionMessageService securityUserExceptionMessageService() {
        return new DefaultSecurityMessageServiceImpl();
    }


    /*
    *    Auth
    * */
    @Bean
    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    public AuthenticationFailureHandler iAuthenticationFailureHandler(ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService) {
        return new DefaultAuthenticationFailureHandlerImpl(iSecurityUserExceptionMessageService);
    }
    @Bean
    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
    public AuthenticationSuccessHandler iAuthenticationSuccessHandler(ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService) {
        return new DefaultAuthenticationSuccessHandlerImpl(iSecurityUserExceptionMessageService);
    }
    @Bean
    @ConditionalOnMissingBean(IOauth2AuthenticationHashCheckService.class)
    public IOauth2AuthenticationHashCheckService iOauth2AuthenticationHashCheckService(ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService) {
        return new DefaultOauth2AuthenticationHashCheckService(passwordEncoder(), iSecurityUserExceptionMessageService);
    }


    /*
     *    Resource
     * */

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint iAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        return new DefaultAuthenticationEntryPoint(resolver);
    }

}
