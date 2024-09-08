package com.autofocus.pms.hq.domain.common.extcode.dao;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.extcode.entity.ExtCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ExtCodeRepository extends JpaRepository<ExtCode, Integer>, QuerydslPredicateExecutor<ExtCode> {
    Optional<ExtCode> findByCodeAndDelYn(String code, YNCode delYn);
}
