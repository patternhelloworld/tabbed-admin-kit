package io.github.patternhelloworld.tak.domain.common.customergroup.dao;

import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.customergroup.entity.CustomerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;


public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Integer>, QuerydslPredicateExecutor<CustomerGroup> {
    Optional<CustomerGroup> findByGroupNmAndDelYn(String groupNm, YNCode delYn);
}