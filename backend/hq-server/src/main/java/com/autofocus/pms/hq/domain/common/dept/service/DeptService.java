package com.autofocus.pms.hq.domain.common.dept.service;

import com.autofocus.pms.common.config.response.error.exception.data.ResourceNotFoundException;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.dealer.dao.DealerRepositorySupport;
import com.autofocus.pms.hq.domain.common.dealer.entity.Dealer;
import com.autofocus.pms.hq.domain.common.dept.dao.DeptRepository;
import com.autofocus.pms.hq.domain.common.dept.dao.DeptRepositorySupport;
import com.autofocus.pms.hq.domain.common.dept.dto.DeptReqDTO;
import com.autofocus.pms.hq.domain.common.dept.dto.DeptResDTO;
import com.autofocus.pms.hq.domain.common.dept.entity.Dept;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptService {
    private final DeptRepository deptRepository;
    private final DeptRepositorySupport deptRepositorySupport;
    private final DealerRepositorySupport dealerRepositorySupport;

    @Transactional(value = "commonTransactionManager", readOnly = true)
    public List<DeptResDTO.IdxNm> getDeptsMetaList(){
        return deptRepositorySupport.findDeptIdxNmList();
    }

    public Page<DeptResDTO.One> getDeptPage(Boolean skipPagination, Integer pageNum, Integer pageSize, String deptSearchFilter, String sorterValueFilter, String dateRangeFilter) throws JsonProcessingException {
        return deptRepositorySupport.findByPageAndFilter(skipPagination, pageNum, pageSize, deptSearchFilter, sorterValueFilter, dateRangeFilter);
    }


    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public DeptResDTO.DeptIdx updateDept(Integer deptIdx, DeptReqDTO.CreateUpdateOne dto,@AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        Dept dept = deptRepository.findById(dto.getDeptIdx()).orElseThrow(() -> new ResourceNotFoundException("Dept for '" + dto.getDeptIdx() + "' not found."));
        return deptRepositorySupport.updateDept(dept, dto, accessTokenUserInfo.getUsername());
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public DeptResDTO.DeptIdx createDept(DeptReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){

        Dealer dealer = dealerRepositorySupport.findById(Math.toIntExact(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getDealerCd()));
        return new DeptResDTO.DeptIdx(deptRepository.save(dto.toEntity(dealer)));
    }
}
