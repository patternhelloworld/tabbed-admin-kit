package com.autofocus.pms.hq.domain.common.stock.dao;

import com.autofocus.pms.hq.domain.common.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long>, QuerydslPredicateExecutor<Stock> {
    Optional<Stock> findByVinIdx(Long vinIdx);
}