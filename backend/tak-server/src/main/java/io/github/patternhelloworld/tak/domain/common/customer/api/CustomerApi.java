package io.github.patternhelloworld.tak.domain.common.customer.api;

import io.github.patternhelloworld.common.config.response.GlobalSuccessPayload;
import io.github.patternhelloworld.common.util.CommonConstant;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.customer.dto.CustomerCommonDTO;
import io.github.patternhelloworld.tak.domain.common.customer.dto.CustomerReqDTO;
import io.github.patternhelloworld.tak.domain.common.customer.service.CustomerService;
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
public class CustomerApi {
    private final CustomerService customerService;

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/customers/list")
    public GlobalSuccessPayload<Page<CustomerCommonDTO.One>> getCustomerPage(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
                                                                             @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
                                                                             @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
                                                                             @RequestParam(value = "customerSearchFilter", required = false) final String customerSearchFilter,
                                                                             @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
                                                                             @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter,
                                                                             @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo)
            throws JsonProcessingException {
        return new GlobalSuccessPayload<>(customerService.getCustomerPage(skipPagination, pageNum, pageSize, customerSearchFilter, sorterValueFilter, dateRangeFilter));
    }


    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PutMapping("/customers/list/{customerIdx}")
    public GlobalSuccessPayload<CustomerCommonDTO.One> updateCustomer(@PathVariable("customerIdx") final Integer customerIdx,
                                                                      @Valid @RequestBody final CustomerCommonDTO.One dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo)  {
        dto.setCustomerIdx(customerIdx);
        return new GlobalSuccessPayload<>(customerService.updateCustomer(dto, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/customers/list/user-managers")
    public GlobalSuccessPayload<CustomerCommonDTO.Updated> updateCustomersUserManager(
            @Valid @RequestBody final CustomerReqDTO.CustomersUserManager dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo)  {

        return new GlobalSuccessPayload<>(customerService.updateCustomersUserManager(dto, accessTokenUserInfo));
    }


    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PostMapping("/customers/list")
    public GlobalSuccessPayload<CustomerCommonDTO.CustomerIdx> createCustomer(@RequestBody final CustomerCommonDTO.One dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(customerService.createCustomer(dto, accessTokenUserInfo));
    }

    /*
     *   복원 가능 한 삭제 (delete)
     * */
    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/customers/list/{customerIdx}/delete")
    public GlobalSuccessPayload<CustomerCommonDTO.CustomerIdx> deleteCustomer(@PathVariable("customerIdx") final Integer customerIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        customerService.deleteCustomer(customerIdx, accessTokenUserInfo.getUsername());

        return new GlobalSuccessPayload<>(new CustomerCommonDTO.CustomerIdx(customerIdx));
    }

    /*
     *   복원 가능 한 삭제 (restore)
     * */
    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/customers/list/{customerIdx}/restore")
    public GlobalSuccessPayload<CustomerCommonDTO.CustomerIdx> restoreCustomer(@PathVariable("customerIdx") final Integer customerIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        customerService.restoreCustomer(customerIdx, accessTokenUserInfo.getUsername());

        return new GlobalSuccessPayload<>(new CustomerCommonDTO.CustomerIdx(customerIdx));
    }

    /*
     *   복원 불가 한 삭제 (destroy)
     * */
    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @DeleteMapping("/customers/list/{customerIdx}")
    public GlobalSuccessPayload<CustomerCommonDTO.CustomerIdx> destroyCustomer(@PathVariable("customerIdx") final Integer customerIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        customerService.destroyCustomer(customerIdx);

        return new GlobalSuccessPayload<>(new CustomerCommonDTO.CustomerIdx(customerIdx));
    }
}
