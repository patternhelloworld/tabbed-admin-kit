package io.github.patternhelloworld.tak.config.securityimpl.service.userdetail;

import io.github.patternhelloworld.tak.config.securityimpl.principal.AdditionalAccessTokenUserInfo;

import io.github.patternknife.securityhelper.oauth2.api.config.security.serivce.userdetail.UserDetailsServiceFactory;
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
