package com.autofocus.pms.hq.domain.common.stock.dao;

import com.autofocus.pms.common.config.response.error.exception.data.ResourceNotFoundException;
import com.autofocus.pms.hq.config.database.CommonQuerydslRepositorySupport;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.stock.entity.Stock;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public class StockRepositorySupport extends CommonQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final StockRepository stockRepository;

    public StockRepositorySupport(StockRepository stockRepository, @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(Stock.class);
        this.stockRepository = stockRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Stock findById(Long stockIdx) throws ResourceNotFoundException {
        return stockRepository.findById(stockIdx).orElseThrow(() -> new ResourceNotFoundException("findById - Code not found for this id :: " + stockIdx));
    }

    // Stock 과 Vin 은 비즈니스 상 1:1 관계 이므로 이 함수는 가능하다.
    public Stock findByVinIdx(Long vinIdx) throws ResourceNotFoundException {
        return stockRepository.findByVinIdx(vinIdx).orElseThrow(() -> new ResourceNotFoundException("차대 번호 (VIN) 를 확인해 주십시오."));
    }
    

    public void deleteOne(Long stockIdx, String modifier) {
        Stock stock = findById(stockIdx);
        stock.setDelDt(LocalDateTime.now());
        stock.setDelYn(YNCode.Y);
        stock.setDelUserid(modifier);
    }


    public void restoreOne(Long stockIdx, String modifier) {
        Stock stock = findById(stockIdx);

        stock.setDelDt(null);
        stock.setDelYn(YNCode.N);
        stock.setDelUserid(modifier);
    }

    public void destroyOne(Long stockIdx) {
        Stock stock = findById(stockIdx);
        stockRepository.delete(stock);
    }


}
