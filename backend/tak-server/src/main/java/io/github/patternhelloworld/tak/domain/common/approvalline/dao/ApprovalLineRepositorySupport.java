package io.github.patternhelloworld.tak.domain.common.approvalline.dao;

import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.tak.config.database.CommonQuerydslRepositorySupport;
import io.github.patternhelloworld.tak.config.database.typeconverter.LineGb;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.approvalline.dto.ApprovalLineReqDTO;
import io.github.patternhelloworld.tak.domain.common.approvalline.dto.ApprovalLineResDTO;
import io.github.patternhelloworld.tak.domain.common.approvalline.dto.QApprovalLineResDTO_DeptAndLineGbs;
import io.github.patternhelloworld.tak.domain.common.approvalline.dto.QApprovalLineResDTO_One;
import io.github.patternhelloworld.tak.domain.common.approvalline.entity.ApprovalLine;
import io.github.patternhelloworld.tak.domain.common.approvalline.entity.QApprovalLine;
import io.github.patternhelloworld.tak.domain.common.dept.entity.QDept;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public class ApprovalLineRepositorySupport extends CommonQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final ApprovalLineRepository approvalLineRepository;

    public ApprovalLineRepositorySupport(ApprovalLineRepository approvalLineRepository, @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(ApprovalLine.class);
        this.approvalLineRepository = approvalLineRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public ApprovalLine findById(Integer approvalLineIdx) throws ResourceNotFoundException {
        return approvalLineRepository.findById(approvalLineIdx).orElseThrow(() -> new ResourceNotFoundException("findById - ApprovalLine not found for this id :: " + approvalLineIdx));
    }

    public List<ApprovalLineResDTO.One> findList(Integer dealerCd) throws JsonProcessingException {
        QApprovalLine qApprovalLine = QApprovalLine.approvalLine;
        QDept qDept = QDept.dept;

        // 서브쿼리: deptIdx와 parentCd가 같은 dept의 parentCd 값을 가져옴
        JPQLQuery<Integer> subQuery = JPAExpressions.select(qDept.parentCd)
                .from(qDept)
                .where(qDept.deptIdx.eq(qDept.parentCd));

        JPQLQuery<ApprovalLineResDTO.One> query = jpaQueryFactory.select(new QApprovalLineResDTO_One(qDept.deptIdx, qDept.deptNm,
                        qApprovalLine.approvalLineIdx, qApprovalLine.dealerCd, qApprovalLine.showroomIdx, qApprovalLine.lineGb,
                        qApprovalLine.lineDetails, qApprovalLine.regUserid, qApprovalLine.regDt, qApprovalLine.modUserid, qApprovalLine.modDt))
                .from(qDept)
                .leftJoin(qApprovalLine).on(qDept.deptIdx.eq(qApprovalLine.showroomIdx))
                .where(
                        qDept.parentCd.in(subQuery),  // 2단계 전시장만 찾는다. (1단계는 딜러명 그대로 1개만 하기로 약속하였다.)
                        qDept.dealerCd.eq(dealerCd),
                        qApprovalLine.delYn.eq(YNCode.N)
                );

        query.orderBy(qDept.deptIdx.asc(), qApprovalLine.lineGb.asc());
        return query.fetch();
    }

    public List<ApprovalLineResDTO.DeptAndLineGbs> findRegistrableLineGbs(Integer dealerCd) {
        QDept qDept = QDept.dept;
        QApprovalLine qApprovalLine = QApprovalLine.approvalLine;

        // 서브쿼리: deptIdx와 parentCd가 같은 dept의 parentCd 값을 가져옴
        JPQLQuery<Integer> subQuery = JPAExpressions.select(qDept.parentCd)
                .from(qDept)
                .where(qDept.deptIdx.eq(qDept.parentCd));

        JPQLQuery<ApprovalLineResDTO.DeptAndLineGbs> query = jpaQueryFactory.select(new QApprovalLineResDTO_DeptAndLineGbs(
                        qDept.deptIdx, qDept.deptNm,
                        new CaseBuilder().when(qApprovalLine.lineGb.eq(LineGb.A)).then(1).otherwise(0).max(),
                        new CaseBuilder().when(qApprovalLine.lineGb.eq(LineGb.C)).then(1).otherwise(0).max(),
                        new CaseBuilder().when(qApprovalLine.lineGb.eq(LineGb.D)).then(1).otherwise(0).max()))
                .from(qDept)
                .leftJoin(qApprovalLine).on(qDept.deptIdx.eq(qApprovalLine.showroomIdx)
                        .and(qApprovalLine.delYn.ne(YNCode.Y)))
                .where(
                        qDept.parentCd.in(subQuery),  // 2단계 전시장만 찾는다. (1단계는 딜러명 그대로 1개만 하기로 약속하였다.)
                        qDept.dealerCd.eq(dealerCd),
                        qDept.delYn.eq(YNCode.N)
                )
                .groupBy(qDept.deptIdx)
                /*.having(
                        new CaseBuilder().when(qApprovalLine.lineGb.eq(LineGb.A)).then(1).otherwise(0).max().eq(0)
                                .or(new CaseBuilder().when(qApprovalLine.lineGb.eq(LineGb.C)).then(1).otherwise(0).max().eq(0))
                                .or(new CaseBuilder().when(qApprovalLine.lineGb.eq(LineGb.D)).then(1).otherwise(0).max().eq(0))
                )*/
                .orderBy(qDept.deptNm.asc());

        return query.fetch();
    }


    public ApprovalLineResDTO.ApprovalLineIdx updateApprovalLine(ApprovalLine approvalLine, ApprovalLineReqDTO.CreateUpdateOne dto, User user) {
        approvalLine.updateApprovalLine(dto, user.getUserId());
        return new ApprovalLineResDTO.ApprovalLineIdx(approvalLine);
    }

    public void deleteOne(ApprovalLine approvalLine, String modifier) {
        approvalLine.setDelDt(LocalDateTime.now());
        approvalLine.setDelYn(YNCode.Y);
        approvalLine.setDelUserid(modifier);
    }

}
