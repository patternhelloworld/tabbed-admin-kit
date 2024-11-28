package io.github.patternhelloworld.tak.domain.common.approvalline.dao;

import io.github.patternhelloworld.tak.config.database.typeconverter.LineGb;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.approvalline.entity.ApprovalLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Integer>, QuerydslPredicateExecutor<ApprovalLine> {
    List<ApprovalLine> findByDelYnAndDealerCdAndShowroomIdx(YNCode ynCode, Integer dealerCd, Integer showroomIdx);

    Optional<ApprovalLine> findApprovalLineIdxByShowroomIdxAndLineGbAndDelYn(Integer showroomIdx, LineGb lineGb, YNCode ynCode);
}