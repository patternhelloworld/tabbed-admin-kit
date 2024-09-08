package com.autofocus.pms.hq.domain.common.dealer.api;

import com.autofocus.pms.common.config.response.GlobalSuccessPayload;
import com.autofocus.pms.common.util.CommonConstant;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.dealer.dto.DealerReqDTO;
import com.autofocus.pms.hq.domain.common.dealer.dto.DealerResDTO;
import com.autofocus.pms.hq.domain.common.dealer.service.DealerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/settings")
@AllArgsConstructor
public class DealerApi {

    private final DealerService dealerService;

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/dealers")
    public GlobalSuccessPayload<Page<DealerResDTO.One>> getDealerPage(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
                                                                       @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
                                                                       @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
                                                                       @RequestParam(value = "dealerSearchFilter", required = false) final String dealerSearchFilter,
                                                                       @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
                                                                       @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter)
            throws JsonProcessingException {
        return new GlobalSuccessPayload<>(dealerService.getDealerPage(skipPagination, pageNum, pageSize, dealerSearchFilter, sorterValueFilter, dateRangeFilter));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/dealers/metas")
    public GlobalSuccessPayload<List<DealerResDTO.CdNm>> getDealerMetaList()
            throws JsonProcessingException {
        return new GlobalSuccessPayload<>(dealerService.getDealerMetaList());
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PutMapping("/dealers/{dealerCd}")
    public GlobalSuccessPayload<DealerResDTO.DealerCd> updateDept(@PathVariable("dealerCd") final Integer dealerCd, @Valid @RequestBody final DealerReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(dealerService.updateDealer(dealerCd, dto));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PostMapping("/dealers")
    public GlobalSuccessPayload<DealerResDTO.DealerCd> createDealer(@RequestBody final DealerReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(dealerService.createDealer(dto, accessTokenUserInfo));
    }

}
