package com.autofocus.pms.security.oauth2.config.security.serivce.userdetail;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailsServiceFactory {
    UserDetailsService getUserDetailsService(String clientId);
}
