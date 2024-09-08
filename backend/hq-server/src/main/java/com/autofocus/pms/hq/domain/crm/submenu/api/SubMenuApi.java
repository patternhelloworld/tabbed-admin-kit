package com.autofocus.pms.hq.domain.crm.submenu.api;


import com.autofocus.pms.common.config.response.GlobalSuccessPayload;
import com.autofocus.pms.common.util.CommonConstant;
import com.autofocus.pms.hq.domain.crm.submenu.dto.SubMenuReqDto;
import com.autofocus.pms.hq.domain.crm.submenu.dto.SubMenuResDto;
import com.autofocus.pms.hq.domain.crm.submenu.service.SubMenuService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SubMenuApi {

    private final SubMenuService subMenuService;

    @PreAuthorize("@customSecurityExpressionService.isAuthenticated()")
    @GetMapping("/settings/menus/subs")
    public GlobalSuccessPayload<List<SubMenuResDto.OneWithMainMenu>> getList(
            @RequestParam(value = "skipPagination", required = false, defaultValue = "false") Boolean skipPagination,
            @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) Integer pageSize,
            @RequestParam(value = "subMenuSearchFilter", required = false) String subMenuSearchFilter,
            @RequestParam(value = "sorterValueFilter", required = false) String sorterValueFilter,
            @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter
            ) throws JsonProcessingException {

        return new GlobalSuccessPayload<>(subMenuService.findSubMenuPage(skipPagination, pageNum, pageSize, subMenuSearchFilter, sorterValueFilter, dateRangeFilter));
    }

    // 일반적으로, PUT 은 전체에 해당하는 변경 사항, PATCH 는 아주 일부분에 해당됩니다.
    @PreAuthorize("@customSecurityExpressionService.isAuthenticated()")
    @PutMapping("/settings/menus/subs/{id}")
    public GlobalSuccessPayload<SubMenuResDto.MinimalOne> updateSubMenu(@PathVariable("id") final Integer id,
                                                                                         @Valid @RequestBody final SubMenuReqDto.Update dto) {
        return new GlobalSuccessPayload<>(subMenuService.updateSubMenu(id, dto));
    }



}
