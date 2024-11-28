package io.github.patternhelloworld.tak.domain.common.dealerstock.dao;

import io.github.patternhelloworld.tak.domain.common.dealerstock.entity.DealerStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DealerStockRepository extends JpaRepository<DealerStock, Long>, QuerydslPredicateExecutor<DealerStock> {

}