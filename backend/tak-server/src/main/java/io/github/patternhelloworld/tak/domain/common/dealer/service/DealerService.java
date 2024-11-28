package io.github.patternhelloworld.tak.domain.common.dealer.service;

import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.dealer.dao.DealerRepository;
import io.github.patternhelloworld.tak.domain.common.dealer.dao.DealerRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.dealer.dto.DealerReqDTO;
import io.github.patternhelloworld.tak.domain.common.dealer.dto.DealerResDTO;
import io.github.patternhelloworld.tak.domain.common.dealer.entity.Dealer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealerService {
    private final DealerRepository dealerRepository;
    private final DealerRepositorySupport dealerRepositorySupport;

    public Page<DealerResDTO.One> getDealerPage(Boolean skipPagination, Integer pageNum, Integer pageSize, String dealerSearchFilter, String sorterValueFilter, String dateRangeFilter) throws JsonProcessingException {
        return dealerRepositorySupport.findByPageAndFilter(skipPagination, pageNum, pageSize, dealerSearchFilter, sorterValueFilter, dateRangeFilter);
    }

    public List<DealerResDTO.CdNm> getDealerMetaList(){
        return dealerRepositorySupport.findDealerCdNmList();
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public DealerResDTO.DealerCd updateDealer(Integer dealerCd, DealerReqDTO.CreateUpdateOne dto) {
        Dealer dealer = dealerRepository.findById(dealerCd).orElseThrow(() -> new ResourceNotFoundException("Dealer for '" + dealerCd + "' not found."));
        return dealerRepositorySupport.updateDealer(dealer, dto);
    }

    public DealerResDTO.DealerCd createDealer(DealerReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){
        return new DealerResDTO.DealerCd(dealerRepository.save(dto.toEntity()));
    }
}
