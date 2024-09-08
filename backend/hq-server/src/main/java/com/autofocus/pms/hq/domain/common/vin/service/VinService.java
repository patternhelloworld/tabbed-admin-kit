package com.autofocus.pms.hq.domain.common.vin.service;

import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.carmodeldetail.dao.CarModelDetailRepository;
import com.autofocus.pms.hq.domain.common.carmodeldetail.entity.CarModelDetail;
import com.autofocus.pms.hq.domain.common.dept.dao.DeptRepositorySupport;
import com.autofocus.pms.hq.domain.common.user.dao.UserRepositorySupport;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import com.autofocus.pms.hq.domain.common.vin.dao.VinRepository;
import com.autofocus.pms.hq.domain.common.vin.dao.VinRepositorySupport;
import com.autofocus.pms.hq.domain.common.vin.dto.VinCommonDTO;
import com.autofocus.pms.hq.domain.common.vin.entity.Vin;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class VinService {

    private final VinRepositorySupport vinRepositorySupport;
    private final VinRepository vinRepository;

    private final CarModelDetailRepository carModelDetailRepository;
    private final DeptRepositorySupport deptRepositorySupport;

    private final UserRepositorySupport userRepositorySupport;

    public Page<VinCommonDTO.One> getVinPage(Boolean skipPagination, Integer pageNum, Integer pageSize, String VinSearchFilter, String sorterValueFilter, String dateRangeFilter) throws JsonProcessingException {
        return vinRepositorySupport.findByPageAndFilter(skipPagination, pageNum, pageSize, VinSearchFilter, sorterValueFilter, dateRangeFilter);
    }


    public VinCommonDTO.VinIdx createVin(VinCommonDTO.One dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){

        CarModelDetail carModelDetail = carModelDetailRepository.findById(dto.getCarModelDetailIdx()).orElse(null);

        User user = userRepositorySupport.findByUserId(accessTokenUserInfo.getUsername());

        Vin Vin = vinRepository.save(dto.toEntity(carModelDetail, user
        ));

        return new VinCommonDTO.VinIdx(Vin.getVinIdx());
    }
    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public VinCommonDTO.One updateVin(VinCommonDTO.One dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        // Vin 엔티티를 가져옵니다.
        Vin vin = vinRepositorySupport.findById(dto.getVinIdx());
        CarModelDetail carModelDetail = carModelDetailRepository.findById(dto.getCarModelDetailIdx()).orElse(null);

        vin.updateVin(dto, carModelDetail, accessTokenUserInfo.getUsername());

        return dto;
    }


    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void deleteVin(Long vinIdx, String modifier){
        vinRepositorySupport.deleteOne(vinIdx, modifier);
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void restoreVin(Long vinIdx, String modifier){
        vinRepositorySupport.restoreOne(vinIdx, modifier);
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void destroyVin(Long vinIdx){
        vinRepositorySupport.destroyOne(vinIdx);
    }

}
