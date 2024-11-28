package io.github.patternhelloworld.tak.domain.common.user.api;

import io.github.patternhelloworld.common.config.response.GlobalSuccessPayload;
import io.github.patternhelloworld.common.config.response.error.CustomExceptionUtils;
import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.common.util.CommonConstant;
import io.github.patternhelloworld.tak.config.logger.module.ResponseSuccessLogConfig;
import io.github.patternhelloworld.tak.config.securityimpl.message.CustomSecurityUserExceptionMessage;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.user.dto.UserCommonDTO;
import io.github.patternhelloworld.tak.domain.common.user.service.UserService;


import io.github.patternknife.securityhelper.oauth2.api.config.security.response.error.exception.KnifeOauth2AuthenticationException;
import io.github.patternknife.securityhelper.oauth2.api.config.security.serivce.persistence.authorization.OAuth2AuthorizationServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


/*
*   User 이 User 을 제어하는 API 들에 한해 SUPER_ADMIN 들만 가능하다.
* */
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class UserApi {

    private final UserService userService;
    private final OAuth2AuthorizationServiceImpl authorizationService;


    @PreAuthorize("@customSecurityExpressionService.isAuthenticated()")
    @GetMapping("/users/me")
    public GlobalSuccessPayload<?> getUserSelf(@AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo,
                                                @Nullable @RequestHeader("Authorization") String authorizationHeader) throws ResourceNotFoundException {
        if(authorizationHeader == null){
            throw new KnifeOauth2AuthenticationException(CustomSecurityUserExceptionMessage.AUTHENTICATION_TOKEN_FAILURE.getMessage());
        }

        String token = authorizationHeader.substring("Bearer ".length());


        int accessTokenRemainingSeconds = 0;

        OAuth2Authorization oAuth2Authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

        if(oAuth2Authorization != null) {
            OAuth2AccessToken oAuth2AccessToken = oAuth2Authorization.getAccessToken().getToken();

            if (oAuth2AccessToken != null) {
                Instant now = Instant.now();
                Instant expiresAt = oAuth2AccessToken.getExpiresAt();
                accessTokenRemainingSeconds = Math.toIntExact(Duration.between(now, expiresAt).getSeconds());

            }
        }

        accessTokenUserInfo.getAdditionalAccessTokenUserInfo().setAccessTokenRemainingSeconds(accessTokenRemainingSeconds);

        return new GlobalSuccessPayload<>(accessTokenUserInfo.getAdditionalAccessTokenUserInfo());

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users/me/logout")
    public GlobalSuccessPayload<?> logoutUser(HttpServletRequest request) {

        DefaultBearerTokenResolver resolver = new DefaultBearerTokenResolver();
        String token = resolver.resolve(request);

        Map<String, Boolean> response = new HashMap<>();

        response.put("logout", Boolean.TRUE);

        try {
            OAuth2Authorization oAuth2Authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

            if(oAuth2Authorization != null) {
                authorizationService.remove(oAuth2Authorization);
            }

        } catch (Exception e) {
            response.put("logout", Boolean.FALSE);
            CustomExceptionUtils.createNonStoppableErrorMessage("로그 아웃 도중 오류 발생", e, LoggerFactory.getLogger(ResponseSuccessLogConfig.class));
        }
        return new GlobalSuccessPayload<>(response);
    }



    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/settings/users")
    public GlobalSuccessPayload<Page<UserCommonDTO.OneWithDeptDealer>> getUserList(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") Boolean skipPagination,
                                                                                   @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) Integer pageNum,
                                                                                   @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) Integer pageSize,
                                                                                   @RequestParam(value = "userSearchFilter", required = false) String userSearchFilter,
                                                                                   @RequestParam(value = "sorterValueFilter", required = false) String sorterValueFilter,
                                                                                   @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter,
                                                                                   @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo)
            throws JsonProcessingException, ResourceNotFoundException {

        return new GlobalSuccessPayload<>(userService.findUsersPage(skipPagination, pageNum, pageSize, userSearchFilter, sorterValueFilter, dateRangeFilter, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PutMapping("/settings/users/{userIdx}")
    public GlobalSuccessPayload<UserCommonDTO.OneWithDeptDealer> updateUser(@PathVariable("userIdx") final long userIdx,
                                                                            @Valid @RequestBody final UserCommonDTO.OneWithDeptDealer dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo)  {
        dto.setUserIdx(userIdx);
        return new GlobalSuccessPayload<>(userService.updateUser(dto, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PostMapping("/settings/users")
    public GlobalSuccessPayload<UserCommonDTO.UserIdx> createUser(@RequestBody final UserCommonDTO.OneWithDeptDealer dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(userService.createUser(dto, accessTokenUserInfo));
    }

    /*
    *   복원 가능 한 삭제 (delete)
    * */
    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/settings/users/{userIdx}/delete")
    public GlobalSuccessPayload<UserCommonDTO.UserIdx> deleteUser(@PathVariable("userIdx") final long userIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        userService.deleteUser(userIdx, accessTokenUserInfo.getUsername());

        return new GlobalSuccessPayload<>(new UserCommonDTO.UserIdx(userIdx));
    }

    /*
     *   복원 가능 한 삭제 (restore)
     * */
    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/settings/users/{userIdx}/restore")
    public GlobalSuccessPayload<UserCommonDTO.UserIdx> restoreUser(@PathVariable("userIdx") final long userIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        userService.restoreUser(userIdx, accessTokenUserInfo.getUsername());

        return new GlobalSuccessPayload<>(new UserCommonDTO.UserIdx(userIdx));
    }

    /*
     *   복원 불가 한 삭제 (destroy)
     * */
    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @DeleteMapping("/settings/users/{userIdx}")
    public GlobalSuccessPayload<UserCommonDTO.UserIdx> destroyUser(@PathVariable("userIdx") final long userIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        userService.destroyUser(userIdx);

        return new GlobalSuccessPayload<>(new UserCommonDTO.UserIdx(userIdx));
    }
}
