package com.autofocus.pms.hq.domain.common.dealerstock.api;

import com.autofocus.pms.common.config.response.GlobalSuccessPayload;
import com.autofocus.pms.common.util.CommonConstant;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.dealerstock.dto.DealerStockCommonDTO;
import com.autofocus.pms.hq.domain.common.dealerstock.service.DealerStockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class DealerStockApi {
    private final DealerStockService dealerStockService;

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/cars/dealer-stocks")
    public GlobalSuccessPayload<Page<DealerStockCommonDTO.One>> getDealerStockPage(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
                                                                                   @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
                                                                                   @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
                                                                                   @RequestParam(value = "dealerStockSearchFilter", required = false) final String dealerStockSearchFilter,
                                                                                   @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
                                                                                   @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter,
                                                                                   @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo)
            throws JsonProcessingException {
        return new GlobalSuccessPayload<>(dealerStockService.getDealerStockPage(skipPagination, pageNum, pageSize, dealerStockSearchFilter, sorterValueFilter, dateRangeFilter));
    }


    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PutMapping("/cars/dealer-stocks/{dealerStockIdx}")
    public GlobalSuccessPayload<DealerStockCommonDTO.One> updateDealerStock(@PathVariable("dealerStockIdx") final Long dealerStockIdx,
                                                                      @Valid @RequestBody final DealerStockCommonDTO.One dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo)  {
        dto.setDealerStockIdx(dealerStockIdx);
        return new GlobalSuccessPayload<>(dealerStockService.updateDealerStock(dto, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PostMapping("/cars/dealer-stocks")
    public GlobalSuccessPayload<DealerStockCommonDTO.DealerStockIdx> createDealerStock(@RequestBody final DealerStockCommonDTO.Create dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(dealerStockService.createDealerStock(dto, accessTokenUserInfo));
    }

    /*
     *   복원 가능 한 삭제 (delete)
     * */
    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/cars/dealer-stocks/{dealerStockIdx}/delete")
    public GlobalSuccessPayload<DealerStockCommonDTO.DealerStockIdx> deleteDealerStock(@PathVariable("dealerStockIdx") final Long dealerStockIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        dealerStockService.deleteDealerStock(dealerStockIdx, accessTokenUserInfo.getUsername());

        return new GlobalSuccessPayload<>(new DealerStockCommonDTO.DealerStockIdx(dealerStockIdx));
    }

    /*
     *   복원 가능 한 삭제 (restore)
     * */
    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/cars/dealer-stocks/{dealerStockIdx}/restore")
    public GlobalSuccessPayload<DealerStockCommonDTO.DealerStockIdx> restoreDealerStock(@PathVariable("dealerStockIdx") final Long dealerStockIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        dealerStockService.restoreDealerStock(dealerStockIdx, accessTokenUserInfo.getUsername());

        return new GlobalSuccessPayload<>(new DealerStockCommonDTO.DealerStockIdx(dealerStockIdx));
    }

    /*
     *   복원 불가 한 삭제 (destroy)
     * */
    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @DeleteMapping("/cars/dealer-stocks/{dealerStockIdx}")
    public GlobalSuccessPayload<DealerStockCommonDTO.DealerStockIdx> destroyDealerStock(@PathVariable("dealerStockIdx") final Long dealerStockIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        dealerStockService.destroyDealerStock(dealerStockIdx);

        return new GlobalSuccessPayload<>(new DealerStockCommonDTO.DealerStockIdx(dealerStockIdx));
    }
}
