package io.github.patternhelloworld.tak.domain.common.stock.dao;

import io.github.patternhelloworld.tak.domain.common.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long>, QuerydslPredicateExecutor<Stock> {
    Optional<Stock> findByVinIdx(Long vinIdx);
}