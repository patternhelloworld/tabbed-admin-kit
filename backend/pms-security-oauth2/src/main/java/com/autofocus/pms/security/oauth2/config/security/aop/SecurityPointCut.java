package com.autofocus.pms.security.oauth2.config.security.aop;

import com.autofocus.pms.security.oauth2.config.security.entity.CustomOauthAccessToken;
import com.autofocus.pms.security.oauth2.config.security.entity.CustomOauthClientDetail;
import com.autofocus.pms.security.oauth2.config.security.entity.CustomOauthRefreshToken;
import jakarta.annotation.Nullable;

public interface SecurityPointCut {
    <T> @Nullable T afterTokensSaved(@Nullable CustomOauthAccessToken customOauthAccessToken, @Nullable CustomOauthRefreshToken customOauthRefreshToken, @Nullable CustomOauthClientDetail customOauthClientDetail);
}
