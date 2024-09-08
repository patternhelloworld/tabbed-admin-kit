package com.autofocus.pms.hq.domain.common.vin.dao;

import com.autofocus.pms.hq.domain.common.vin.entity.Vin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface VinRepository extends JpaRepository<Vin, Long>, QuerydslPredicateExecutor<Vin> {

}