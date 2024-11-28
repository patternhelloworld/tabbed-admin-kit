package io.github.patternhelloworld.tak.domain.crm.usermenuauth.api;

import io.github.patternhelloworld.common.config.response.GlobalSuccessPayload;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.crm.usermenuauth.dto.UserMenuAuthCommonDTO;
import io.github.patternhelloworld.tak.domain.crm.usermenuauth.service.UserMenuAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserMenuAuthApi {

    private final UserMenuAuthService userMenuAuthService;

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/settings/users/menus")
    public GlobalSuccessPayload<List<UserMenuAuthCommonDTO.OneWithSubMenu>> getUserMenuAuthsByUserIdx(@RequestParam(value = "userIdx", required = true, defaultValue = "false") final long userIdx) throws JsonProcessingException {
        return new GlobalSuccessPayload<>(userMenuAuthService.findUserMenuAuthByUserIdx(userIdx));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PutMapping("/settings/users/menus/{userIdx}")
    public GlobalSuccessPayload<List<UserMenuAuthCommonDTO.OneWithSubMenu>> updateUserMenuAuths(@PathVariable("userIdx") final long userIdx,
                                                                                                @Valid @RequestBody final UserMenuAuthCommonDTO.PermissionSet permissionSet, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) throws JsonProcessingException {
        return new GlobalSuccessPayload<>(userMenuAuthService.updateUserMenuAuths(permissionSet,accessTokenUserInfo));
    }
}