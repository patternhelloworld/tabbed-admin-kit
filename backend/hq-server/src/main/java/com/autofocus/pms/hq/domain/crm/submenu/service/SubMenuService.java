package com.autofocus.pms.hq.domain.crm.submenu.service;

import com.autofocus.pms.common.util.CommonConstant;
import com.autofocus.pms.hq.domain.crm.submenu.dao.SubMenuRepositorySupport;
import com.autofocus.pms.hq.domain.crm.submenu.dto.SubMenuReqDto;
import com.autofocus.pms.hq.domain.crm.submenu.dto.SubMenuResDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubMenuService {
    private final SubMenuRepositorySupport subMenuRepositorySupport;
    public List<SubMenuResDto.OneWithMainMenu> findSubMenuPage(Boolean skipPagination,
                                                               Integer pageNum,
                                                               Integer pageSize,
                                                               String subMenuSearchFilter,
                                                               String sorterValueFilter,
                                                               String dateRangeFilter) throws JsonProcessingException {

        if(skipPagination){
            pageNum = Integer.valueOf(CommonConstant.COMMON_PAGE_NUM);
            pageSize = Integer.valueOf(CommonConstant.COMMON_PAGE_SIZE_DEFAULT_MAX);
        }
        ObjectMapper objectMapper = new ObjectMapper();

        return subMenuRepositorySupport.getSubMenusPage(pageNum,pageSize,subMenuSearchFilter,sorterValueFilter,dateRangeFilter);
    }


    // commonTransactionManager 는 domain/common 을 담당하므로 여기에서는 작동하지 않습니다.
    @Transactional(value = "crmTransactionManager", rollbackFor=Exception.class)
    public SubMenuResDto.MinimalOne updateSubMenu(Integer id, SubMenuReqDto.Update dto) {
        return subMenuRepositorySupport.updateOne(id, dto);
    }
}
