package io.github.patternhelloworld.tak.domain.common.vin.dao;

import io.github.patternhelloworld.tak.domain.common.vin.entity.Vin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface VinRepository extends JpaRepository<Vin, Long>, QuerydslPredicateExecutor<Vin> {

}