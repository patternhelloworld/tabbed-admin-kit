package com.autofocus.pms.security.oauth2.config.security.serivce.userdetail;

import com.autofocus.pms.common.config.response.error.dto.ErrorMessages;
import com.autofocus.pms.security.oauth2.config.security.response.common.error.auth.CustomOauth2AuthenticationException;
import com.autofocus.pms.security.oauth2.config.security.message.DefaultSecurityUserExceptionMessage;
import com.autofocus.pms.security.oauth2.config.security.message.ISecurityUserExceptionMessageService;
import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConditionalDetailsService {

    private final UserDetailsServiceFactory userDetailsServiceFactory;
    private final ISecurityUserExceptionMessageService iSecurityUserExceptionMessageService;

    public UserDetails loadUserByUsername(String username, String clientId) throws UsernameNotFoundException, CustomOauth2AuthenticationException {

        UserDetailsService userDetailsService = userDetailsServiceFactory.getUserDetailsService(clientId);
        if (userDetailsService != null) {
            return userDetailsService.loadUserByUsername(username);
        }
        throw new CustomOauth2AuthenticationException(ErrorMessages.builder()
                .message("Unable to distinguish UserDetailsService. (username : " + username + " / client_id: " + clientId + ")")
                .userMessage(iSecurityUserExceptionMessageService.getUserMessage(DefaultSecurityUserExceptionMessage.AUTHENTICATION_LOGIN_ERROR))
                .build());

    }
}
