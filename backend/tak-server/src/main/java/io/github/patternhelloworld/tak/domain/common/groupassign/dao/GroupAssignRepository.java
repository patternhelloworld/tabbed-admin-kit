package io.github.patternhelloworld.tak.domain.common.groupassign.dao;

import io.github.patternhelloworld.tak.domain.common.groupassign.entity.GroupAssign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;



public interface GroupAssignRepository extends JpaRepository<GroupAssign, Integer>, QuerydslPredicateExecutor<GroupAssign> {
}