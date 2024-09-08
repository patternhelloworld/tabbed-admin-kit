package com.autofocus.pms.hq.domain.common.dealerstock.dao;

import com.autofocus.pms.hq.domain.common.dealerstock.entity.DealerStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DealerStockRepository extends JpaRepository<DealerStock, Long>, QuerydslPredicateExecutor<DealerStock> {

}