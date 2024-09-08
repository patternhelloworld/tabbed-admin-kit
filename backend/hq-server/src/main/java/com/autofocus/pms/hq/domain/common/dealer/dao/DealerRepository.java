package com.autofocus.pms.hq.domain.common.dealer.dao;

import com.autofocus.pms.hq.domain.common.dealer.entity.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DealerRepository extends JpaRepository<Dealer, Integer>, QuerydslPredicateExecutor<Dealer> {
}