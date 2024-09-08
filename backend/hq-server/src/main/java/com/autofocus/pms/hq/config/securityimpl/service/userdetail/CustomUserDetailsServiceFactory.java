package com.autofocus.pms.hq.config.securityimpl.service.userdetail;

import com.autofocus.pms.hq.config.securityimpl.principal.AdditionalAccessTokenUserInfo;

import com.autofocus.pms.security.oauth2.config.security.serivce.userdetail.UserDetailsServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomUserDetailsServiceFactory implements UserDetailsServiceFactory {

    private final Map<String, UserDetailsService> userDetailsServiceMap;

    @Autowired
    public CustomUserDetailsServiceFactory(List<UserDetailsService> userDetailsServices) {
        userDetailsServiceMap = new HashMap<>();
        for (UserDetailsService userDetailsService : userDetailsServices) {
            if (userDetailsService instanceof UserDetailsServiceImpl) {
                userDetailsServiceMap.put(AdditionalAccessTokenUserInfo.UserType.USER.getValue(), userDetailsService);
            }
        }
    }

    @Override
    public UserDetailsService getUserDetailsService(String clientId) {
        return userDetailsServiceMap.get(clientId);
    }
}
