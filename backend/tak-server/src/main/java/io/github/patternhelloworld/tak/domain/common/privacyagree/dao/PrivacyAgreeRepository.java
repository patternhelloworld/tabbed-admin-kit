package io.github.patternhelloworld.tak.domain.common.privacyagree.dao;

import io.github.patternhelloworld.tak.domain.common.privacyagree.entity.PrivacyAgree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PrivacyAgreeRepository extends JpaRepository<PrivacyAgree, Integer>, QuerydslPredicateExecutor<PrivacyAgree> {
}
