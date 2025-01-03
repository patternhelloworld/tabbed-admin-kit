package io.github.patternhelloworld.tak.domain.common.relocatelog.dao;

import io.github.patternhelloworld.tak.domain.common.relocatelog.entity.RelocateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface RelocateLogRepository extends JpaRepository<RelocateLog, Integer>, QuerydslPredicateExecutor<RelocateLog> {
}