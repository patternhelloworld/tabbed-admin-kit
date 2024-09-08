package com.autofocus.pms.hq.domain.common.groupassign.api;

import com.autofocus.pms.common.config.response.GlobalSuccessPayload;
import com.autofocus.pms.common.util.CommonConstant;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.groupassign.dto.GroupAssignResDTO;
import com.autofocus.pms.hq.domain.common.groupassign.service.GroupAssignService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class GroupAssignApi {
    private final GroupAssignService groupAssignService;

    //@PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/customers/group-assign")
    public GlobalSuccessPayload<Page<GroupAssignResDTO.One>> getGroupedCustomers(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
                                                                                 @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
                                                                                 @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
                                                                                 @RequestParam(value = "groupAssignSearchFilter", required = false) final String groupAssignSearchFilter,
                                                                                 @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
                                                                                 @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter,
                                                                                 @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo)
            throws JsonProcessingException {
        return new GlobalSuccessPayload<>(groupAssignService.getGroupedCustomers(skipPagination, pageNum, pageSize, groupAssignSearchFilter, sorterValueFilter, dateRangeFilter));
    }

}
