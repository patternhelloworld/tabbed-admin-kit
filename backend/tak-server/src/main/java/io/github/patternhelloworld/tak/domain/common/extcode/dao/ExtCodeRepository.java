package io.github.patternhelloworld.tak.domain.common.extcode.dao;

import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.extcode.entity.ExtCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ExtCodeRepository extends JpaRepository<ExtCode, Integer>, QuerydslPredicateExecutor<ExtCode> {
    Optional<ExtCode> findByCodeAndDelYn(String code, YNCode delYn);
}
