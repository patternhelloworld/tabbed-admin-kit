package io.github.patternhelloworld.tak.domain.common.dept.service;

import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.dealer.dao.DealerRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.dealer.entity.Dealer;
import io.github.patternhelloworld.tak.domain.common.dept.dao.DeptRepository;
import io.github.patternhelloworld.tak.domain.common.dept.dao.DeptRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.dept.dto.DeptReqDTO;
import io.github.patternhelloworld.tak.domain.common.dept.dto.DeptResDTO;
import io.github.patternhelloworld.tak.domain.common.dept.entity.Dept;
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
