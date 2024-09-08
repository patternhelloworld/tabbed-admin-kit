package com.autofocus.pms.hq.domain.common.privacyagree.api;

import com.autofocus.pms.common.config.response.GlobalSuccessPayload;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.privacyagree.dto.PrivacyAgreeReqDTO;
import com.autofocus.pms.hq.domain.common.privacyagree.dto.PrivacyAgreeResDTO;
import com.autofocus.pms.hq.domain.common.privacyagree.entity.PrivacyAgree;
import com.autofocus.pms.hq.domain.common.privacyagree.service.PrivacyAgreeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
