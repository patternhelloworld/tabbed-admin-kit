package io.github.patternhelloworld.tak.config.securityimpl.service.permission;

import io.github.patternhelloworld.common.config.HttpMethodType;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.config.securityimpl.message.CustomSecurityUserExceptionMessage;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.config.securityimpl.service.userdetail.UserDetailsServiceImpl;
import io.github.patternhelloworld.tak.domain.common.user.dto.UserCommonDTO;

import io.github.patternknife.securityhelper.oauth2.api.config.security.response.error.exception.KnifeOauth2AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class CustomSecurityExpressionService implements SecurityExpressionOperations {

    /*
    *
    *   Default 인 @PreAuthorize("isAuthenticated()") 는 "헤더에 Bearer 토큰을 명시하지 않을 경우 (Bearer 토큰이 만료되거나, 잘못된 경우는 정상적으로 401)"
    *   Spring Security 에서 Oauth2AccessDeniedException (403) 만을 Throw 해서, 이는 아래의 hasAnyUserRole() 과 같은 권한 확인 함수들과
    *   구분할 수 없다.
    *
    *   따라서 @PreAuthorize("isAuthenticated()") 대신에 여기 @PreAuthorize("@authorityService.isAuthenticated()") 을 사용하여
    *       Oauth2AuthenticationException (401) 를 Throw 한다.
    *
    * */
    public boolean isAuthenticated() throws OAuth2AuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            throw new KnifeOauth2AuthenticationException(CustomSecurityUserExceptionMessage.AUTHENTICATION_TOKEN_FAILURE.getMessage());
        }
        return true;
    }

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public boolean isAuthorized() throws OAuth2AuthenticationException {
        // 인증 확인
        if (!isAuthenticated()) {
            return false;
        }

        // 현재 요청된 URL과 HTTP 메서드를 가져옴
        String currentPath = request.getRequestURI();
        HttpMethodType httpMethodType = getHttpMethodType(request.getMethod());

        // 추가 권한 검사
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        AccessTokenUserInfo userInfo = (AccessTokenUserInfo) userDetails;

        // 경로 권한 검사
        boolean pathMatched = userInfo.getAdditionalAccessTokenUserInfo().getInfo().getPermissions().stream()
                .anyMatch(permission ->
                    currentPath.contains(permission.getMainMenuPath() + "/" + permission.getSubMenuPath()) ||
                    currentPath.contains("files/" + permission.getSubMenuPath()) //파일관련
                );

        if (!pathMatched) {
            return false;
        }

        // CRUD 권한 검사
        boolean crudMatched = checkCrudPermissions(httpMethodType, userInfo);

        if (!crudMatched) {
            return false;
        }

        return true; // 기본적으로 인증된 경우
    }


    private HttpMethodType getHttpMethodType(String method) {
        try {
            return HttpMethodType.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            return HttpMethodType.OTHER;
        }
    }

    private boolean checkCrudPermissions(HttpMethodType httpMethodType, AccessTokenUserInfo userInfo) {
        Stream<UserCommonDTO.OneWithDeptDealerMenus.Permission> permissions = userInfo.getAdditionalAccessTokenUserInfo().getInfo().getPermissions().stream();

        switch (httpMethodType) {
            case GET:
                return permissions.anyMatch(permission -> permission.getYnLst() == YNCode.Y);
            case POST:
                return permissions.anyMatch(permission -> permission.getYnInt() == YNCode.Y);
            case PUT:
            case PATCH:
                return permissions.anyMatch(permission -> permission.getYnMod() == YNCode.Y);
            case DELETE:
                return permissions.anyMatch(permission -> permission.getYnDel() == YNCode.Y);
            default:
                return false;
        }
    }


    /*
    *   여기는 현재 불필요 하여 미사용
    * */
    @Override
    public Authentication getAuthentication() {
        return null;
    }

    @Override
    public boolean hasAuthority(String authority) {
        return false;
    }

    @Override
    public boolean hasAnyAuthority(String... authorities) {
        return false;
    }

    @Override
    public boolean hasRole(String role) {
        return false;
    }

    @Override
    public boolean hasAnyRole(String... roles) {
        return false;
    }

    @Override
    public boolean permitAll() {
        return false;
    }

    @Override
    public boolean denyAll() {
        return false;
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }


    @Override
    public boolean isRememberMe() {
        return false;
    }

    @Override
    public boolean isFullyAuthenticated() {
        return isAuthenticated();
    }

    @Override
    public boolean hasPermission(Object target, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Object targetId, String targetType, Object permission) {
        return false;
    }









}