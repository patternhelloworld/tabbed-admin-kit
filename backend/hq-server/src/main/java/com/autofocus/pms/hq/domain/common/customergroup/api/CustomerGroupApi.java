package com.autofocus.pms.hq.domain.common.customergroup.api;

import com.autofocus.pms.common.config.response.GlobalSuccessPayload;
import com.autofocus.pms.common.util.CommonConstant;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.customergroup.dto.CustomerGroupReqDTO;
import com.autofocus.pms.hq.domain.common.customergroup.dto.CustomerGroupResDTO;
import com.autofocus.pms.hq.domain.common.customergroup.service.CustomerGroupService;
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
public class CustomerGroupApi {
    private final CustomerGroupService customerGroupService;

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/customers/groups")
    public GlobalSuccessPayload<Page<CustomerGroupResDTO.One>> getCustomerGroupPage(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
                                                                                   @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
                                                                                   @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
                                                                                   @RequestParam(value = "customerGroupSearchFilter", required = false) final String customerGroupSearchFilter,
                                                                                   @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
                                                                                   @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter)
            throws JsonProcessingException {
        return new GlobalSuccessPayload<>(customerGroupService.getCustomerGroupPage(skipPagination, pageNum, pageSize, customerGroupSearchFilter, sorterValueFilter, dateRangeFilter));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PostMapping("/customers/groups")
    public GlobalSuccessPayload<CustomerGroupResDTO.Idx> createCustomerGroup(@Valid @RequestBody final CustomerGroupReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(customerGroupService.createCustomerGroup(dto, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PutMapping("/customers/groups/{customerGroupIdx}")
    public GlobalSuccessPayload<CustomerGroupResDTO.Idx> updateCustomerGroup(@PathVariable("customerGroupIdx") final Integer customerGroupIdx, @Valid @RequestBody final CustomerGroupReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(customerGroupService.updateCustomerGroup(customerGroupIdx, dto, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/customers/groups/{customerGroupIdx}/delete")
    public GlobalSuccessPayload<CustomerGroupResDTO.Idx> deleteCustomerGroup(@PathVariable("customerGroupIdx") final Integer customerGroupIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        customerGroupService.deleteCustomerGroup(customerGroupIdx, accessTokenUserInfo);
        return new GlobalSuccessPayload<>(new CustomerGroupResDTO.Idx(customerGroupIdx));
    }

}
