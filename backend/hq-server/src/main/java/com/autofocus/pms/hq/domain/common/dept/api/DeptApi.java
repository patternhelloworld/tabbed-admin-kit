package com.autofocus.pms.hq.domain.common.dept.api;

import com.autofocus.pms.common.config.response.GlobalSuccessPayload;
import com.autofocus.pms.common.util.CommonConstant;
import com.autofocus.pms.common.util.CustomUtils;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.dept.dto.DeptReqDTO;
import com.autofocus.pms.hq.domain.common.dept.dto.DeptResDTO;
import com.autofocus.pms.hq.domain.common.dept.dto.DeptSearchFilter;
import com.autofocus.pms.hq.domain.common.dept.service.DeptService;
import com.autofocus.pms.hq.domain.common.user.dto.UserCommonDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class DeptApi {
    private final DeptService deptService;

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/depts")
    public GlobalSuccessPayload<Page<DeptResDTO.One>> getDeptPage(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
                                                                    @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
                                                                    @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
                                                                    @RequestParam(value = "deptSearchFilter", required = false) final String deptSearchFilter,
                                                                    @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
                                                                    @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter)
            throws JsonProcessingException {
        return new GlobalSuccessPayload<>(deptService.getDeptPage(skipPagination, pageNum, pageSize, deptSearchFilter, sorterValueFilter, dateRangeFilter));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/depts/dealers/me")
    public GlobalSuccessPayload<Page<DeptResDTO.One>> getDeptPageMe(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
                                                                  @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
                                                                  @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
                                                                  @RequestParam(value = "deptSearchFilter", required = false) final String deptSearchFilter,
                                                                  @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
                                                                  @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter,
                                                                  @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo)
            throws JsonProcessingException {


        UserCommonDTO.OneWithDeptDealerMenus oneWithDeptDealerMenus = accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo();

        ObjectMapper objectMapper = new ObjectMapper();

        DeptSearchFilter deptSearchFilterLocal;
        if(!CustomUtils.isEmpty(deptSearchFilter)){
            deptSearchFilterLocal = objectMapper.readValue(deptSearchFilter, DeptSearchFilter.class);
        }else{
            deptSearchFilterLocal = new DeptSearchFilter();
        }
        deptSearchFilterLocal.setDealerCd(oneWithDeptDealerMenus.getDealerCd());

        return new GlobalSuccessPayload<>(deptService.getDeptPage(skipPagination, pageNum, pageSize, objectMapper.writeValueAsString(deptSearchFilterLocal), sorterValueFilter, dateRangeFilter));
    }


    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PutMapping("/depts/{deptIdx}")
    public GlobalSuccessPayload<DeptResDTO.DeptIdx> updateDept(@PathVariable("deptIdx") final Integer deptIdx, @Valid @RequestBody final DeptReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(deptService.updateDept(deptIdx, dto, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/depts/metas")
    public GlobalSuccessPayload<List<DeptResDTO.IdxNm>> getDeptsMetaList()
            throws JsonProcessingException {
        return new GlobalSuccessPayload<>(deptService.getDeptsMetaList());
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PostMapping("/depts")
    public GlobalSuccessPayload<DeptResDTO.DeptIdx> createDept(@RequestBody final DeptReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(deptService.createDept(dto, accessTokenUserInfo));
    }
}
