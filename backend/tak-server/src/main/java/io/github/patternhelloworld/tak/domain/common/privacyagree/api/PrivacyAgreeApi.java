package io.github.patternhelloworld.tak.domain.common.privacyagree.api;

import io.github.patternhelloworld.common.config.response.GlobalSuccessPayload;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.privacyagree.dto.PrivacyAgreeReqDTO;
import io.github.patternhelloworld.tak.domain.common.privacyagree.dto.PrivacyAgreeResDTO;
import io.github.patternhelloworld.tak.domain.common.privacyagree.service.PrivacyAgreeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class PrivacyAgreeApi {
    private final PrivacyAgreeService privacyAgreeService;

    @PostMapping("/customers/privacyagree")
    public GlobalSuccessPayload<PrivacyAgreeResDTO.Idx> createPrivacyAgree(@Valid @RequestBody final PrivacyAgreeReqDTO.CreateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(privacyAgreeService.createPrivacyAgree(dto, accessTokenUserInfo));
    }

    @GetMapping("/customers/privacyagree/{customerIdx}")
    public GlobalSuccessPayload<PrivacyAgreeResDTO.One> getOne(@PathVariable("customerIdx") final Integer customerIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(privacyAgreeService.getOne(customerIdx));
    }
}
