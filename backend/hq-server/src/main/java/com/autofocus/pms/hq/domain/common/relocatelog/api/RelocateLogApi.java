package com.autofocus.pms.hq.domain.common.relocatelog.api;

import com.autofocus.pms.common.config.response.GlobalSuccessPayload;
import com.autofocus.pms.common.util.CommonConstant;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.relocatelog.dto.RelocateLogResDTO;
import com.autofocus.pms.hq.domain.common.relocatelog.service.RelocateLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class RelocateLogApi {
    private final RelocateLogService relocateLogService;

    //@PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/customers/relocated")
    public GlobalSuccessPayload<Page<RelocateLogResDTO.One>> getRelocatedCustomerPage(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
                                                                             @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
                                                                             @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
                                                                             @RequestParam(value = "customerSearchFilter", required = false) final String customerSearchFilter,
                                                                             @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
                                                                             @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter,
                                                                             @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo)
            throws JsonProcessingException {
        return new GlobalSuccessPayload<>(relocateLogService.getRelocatedCustomerPage(skipPagination, pageNum, pageSize, customerSearchFilter, sorterValueFilter, dateRangeFilter));
    }







}
