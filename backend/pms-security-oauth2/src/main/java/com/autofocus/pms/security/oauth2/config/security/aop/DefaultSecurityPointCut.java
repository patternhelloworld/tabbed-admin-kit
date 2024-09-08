package com.autofocus.pms.security.oauth2.config.security.aop;

import com.autofocus.pms.security.oauth2.config.security.entity.CustomOauthAccessToken;
import com.autofocus.pms.security.oauth2.config.security.entity.CustomOauthRefreshToken;
import com.autofocus.pms.security.oauth2.config.security.entity.CustomOauthClientDetail;
import jakarta.annotation.Nullable;

public class DefaultSecurityPointCut implements SecurityPointCut {
    @Override
    public <T> T afterTokensSaved(@Nullable CustomOauthAccessToken customOauthAccessToken, @Nullable CustomOauthRefreshToken customOauthRefreshToken, @Nullable CustomOauthClientDetail customOauthClientDetail) {
        // 기본 동작: 아무 작업도 하지 않음
        return null;
    }
}
