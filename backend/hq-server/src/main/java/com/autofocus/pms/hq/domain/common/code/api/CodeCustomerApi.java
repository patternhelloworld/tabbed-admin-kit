package com.autofocus.pms.hq.domain.common.code.api;

import com.autofocus.pms.common.config.response.GlobalSuccessPayload;
import com.autofocus.pms.common.util.CommonConstant;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.code.dto.CodeCustomerReqDTO;
import com.autofocus.pms.hq.domain.common.code.dto.CodeCustomerResDTO;
import com.autofocus.pms.hq.domain.common.code.entity.CodeCustomer;
import com.autofocus.pms.hq.domain.common.code.service.CodeCustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/codes")
@AllArgsConstructor
public class CodeCustomerApi {
    private final CodeCustomerService codeCustomerService;

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/customer")
    public GlobalSuccessPayload<Page<CodeCustomerResDTO.One>> getCustomerCodePage(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
                                                                          @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
                                                                          @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
                                                                          @RequestParam(value = "codeCustomerSearchFilter", required = false) final String codeCustomerSearchFilter,
                                                                          @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
                                                                          @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter)
            throws JsonProcessingException {
        return new GlobalSuccessPayload<>(codeCustomerService.getCustomerCodePage(skipPagination, pageNum, pageSize, codeCustomerSearchFilter, sorterValueFilter, dateRangeFilter));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PostMapping("/customer")
    public GlobalSuccessPayload<CodeCustomerResDTO.Idx> createCustomerCode(@Valid @RequestBody final CodeCustomerReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(codeCustomerService.createCustomerCode(dto, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PutMapping("/customer/{codeCustomerIdx}")
    public GlobalSuccessPayload<CodeCustomerResDTO.Idx> updateCustomerCode(@PathVariable("codeCustomerIdx") final Integer codeCustomerIdx, @Valid @RequestBody final CodeCustomerReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(codeCustomerService.updateCustomerCode(codeCustomerIdx, dto, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/customer/{codeCustomerIdx}/delete")
    public GlobalSuccessPayload<CodeCustomerResDTO.Idx> deleteCustomerCode(@PathVariable("codeCustomerIdx") final Integer codeCustomerIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        codeCustomerService.deleteCodeCustomer(codeCustomerIdx, accessTokenUserInfo);
        return new GlobalSuccessPayload<>(new CodeCustomerResDTO.Idx(codeCustomerIdx));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/customer/{codeCustomerIdx}/metas")
    public GlobalSuccessPayload<List<CodeCustomer>> getCustomerCodeMetas(@PathVariable("codeCustomerIdx") final Integer codeCustomerIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(codeCustomerService.getCustomerCodeMetas(codeCustomerIdx));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PostMapping("/customer/{codeCustomerIdx}/metas")
    public GlobalSuccessPayload<CodeCustomerResDTO.Idx> createMetaCustomerCode(@PathVariable("codeCustomerIdx") final Integer codeCustomerIdx, @Valid @RequestBody final CodeCustomerReqDTO.MetaCreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(codeCustomerService.createMetaCustomerCode(codeCustomerIdx, dto, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PutMapping("/customer/{codeCustomerIdx}/metas")
    public GlobalSuccessPayload<CodeCustomerResDTO.Idx> updateMetaCustomerCode(@PathVariable("codeCustomerIdx") final Integer codeCustomerIdx, @Valid @RequestBody final CodeCustomerReqDTO.MetaCreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(codeCustomerService.updateMetaCustomerCode(codeCustomerIdx, dto, accessTokenUserInfo));
    }
}
