package com.autofocus.pms.hq.domain.common.privacyagree.dao;

import com.autofocus.pms.hq.domain.common.privacyagree.dto.PrivacyAgreeResDTO;
import com.autofocus.pms.hq.domain.common.privacyagree.entity.PrivacyAgree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface PrivacyAgreeRepository extends JpaRepository<PrivacyAgree, Integer>, QuerydslPredicateExecutor<PrivacyAgree> {
}
