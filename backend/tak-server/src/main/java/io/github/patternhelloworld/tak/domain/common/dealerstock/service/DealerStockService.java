package io.github.patternhelloworld.tak.domain.common.dealerstock.service;

import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.dealerstock.dao.DealerStockRepository;
import io.github.patternhelloworld.tak.domain.common.dealerstock.dao.DealerStockRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.dealerstock.dto.DealerStockCommonDTO;
import io.github.patternhelloworld.tak.domain.common.dealerstock.entity.DealerStock;
import io.github.patternhelloworld.tak.domain.common.dept.dao.DeptRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.dept.entity.Dept;
import io.github.patternhelloworld.tak.domain.common.stock.dao.StockRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.stock.entity.Stock;
import io.github.patternhelloworld.tak.domain.common.user.dao.UserRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DealerStockService {

    private final DealerStockRepositorySupport dealerStockRepositorySupport;
    private final DealerStockRepository dealerStockRepository;

    private final StockRepositorySupport stockRepositorySupport;
    private final DeptRepositorySupport deptRepositorySupport;

    private final UserRepositorySupport userRepositorySupport;

    public Page<DealerStockCommonDTO.One> getDealerStockPage(Boolean skipPagination, Integer pageNum, Integer pageSize, String DealerStockSearchFilter, String sorterValueFilter, String dateRangeFilter) throws JsonProcessingException {
        return dealerStockRepositorySupport.findByPageAndFilter(skipPagination, pageNum, pageSize, DealerStockSearchFilter, sorterValueFilter, dateRangeFilter);
    }


    public DealerStockCommonDTO.DealerStockIdx createDealerStock(DealerStockCommonDTO.Create dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){

        Stock stock = stockRepositorySupport.findByVinIdx(dto.getVinIdx());
        Dept dept = deptRepositorySupport.findByIdWithoutThrow(dto.getDeptIdx());

        User user = userRepositorySupport.findByUserId(accessTokenUserInfo.getUsername());

        DealerStock DealerStock = dealerStockRepository.save(dto.toEntity(stock,dept,user
        ));

        return new DealerStockCommonDTO.DealerStockIdx(DealerStock.getDealerStockIdx());
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public DealerStockCommonDTO.One updateDealerStock(DealerStockCommonDTO.One dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        // DealerStock 엔티티를 가져옵니다.
        DealerStock dealerStock = dealerStockRepositorySupport.findById(dto.getDealerStockIdx());
        Dept dept = deptRepositorySupport.findById(dto.getDeptIdx());

        dealerStock.updateDealerStock(dto, dept, accessTokenUserInfo.getUsername());

        return dto;
    }


    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void deleteDealerStock(Long dealerStockIdx, String modifier){
        dealerStockRepositorySupport.deleteOne(dealerStockIdx, modifier);
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void restoreDealerStock(Long dealerStockIdx, String modifier){
        dealerStockRepositorySupport.restoreOne(dealerStockIdx, modifier);
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void destroyDealerStock(Long dealerStockIdx){
        dealerStockRepositorySupport.destroyOne(dealerStockIdx);
    }

}
