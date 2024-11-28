package io.github.patternhelloworld.tak.domain.common.dept.dao;

import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.common.domain.common.dto.DateRangeFilter;
import io.github.patternhelloworld.common.domain.common.dto.SorterValueFilter;
import io.github.patternhelloworld.common.util.CustomUtils;
import io.github.patternhelloworld.common.util.PaginationUtil;
import io.github.patternhelloworld.tak.config.database.CommonQuerydslRepositorySupport;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.dealer.entity.QDealer;
import io.github.patternhelloworld.tak.domain.common.dept.dto.*;
import io.github.patternhelloworld.tak.domain.common.dept.dto.DeptReqDTO;
import io.github.patternhelloworld.tak.domain.common.dept.dto.DeptResDTO;
import io.github.patternhelloworld.tak.domain.common.dept.dto.DeptSearchFilter;
import io.github.patternhelloworld.tak.domain.common.dept.entity.Dept;
import io.github.patternhelloworld.tak.domain.common.dept.entity.QDept;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class DeptRepositorySupport extends CommonQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final DeptRepository deptRepository;

    public DeptRepositorySupport(DeptRepository deptRepository, @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(Dept.class);
        this.deptRepository = deptRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }


    public Dept findById(Integer id) throws ResourceNotFoundException {
        return deptRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 ID 의 조직을 찾을 수 없습니다. :: " + id));
    }
    public Dept findByIdWithoutThrow(Integer id) throws ResourceNotFoundException {
        if(id == null){
            return null;
        }
        return deptRepository.findById(id).orElse(null);
    }

    public Page<DeptResDTO.One> findByPageAndFilter(Boolean skipPagination,
                                                    Integer pageNum,
                                                    Integer pageSize,
                                                    String deptSearchFilter,
                                                    String sorterValueFilter,
                                                    String dateRangeFilter) throws JsonProcessingException {
        QDept qDept = QDept.dept;
        QDealer qDealer = QDealer.dealer;


        JPQLQuery<DeptResDTO.One> query = jpaQueryFactory.select(new QDeptResDTO_One(qDept.deptIdx, qDept.dealerCd, qDealer.dealerNm, qDept.deptNm, qDept.parentCd,
                qDept.menuNum, qDept.viewYn, qDept.deptSort, qDept.selfNm, qDept.selfBizNo, qDept.selfCorporationNo, qDept.selfChiefNm,
                qDept.selfUptae, qDept.selfUpjong, qDept.selfRegId, qDept.selfManager, qDept.selfHphone, qDept.selfFax, qDept.selfZipcode, qDept.selfAddr1,
                qDept.selfAddr2, qDept.selfEmail, qDept.selfImgFname, qDept.selfImgSname, qDept.regUserId, qDept.regDt, qDept.regIp,
                qDept.modUserId, qDept.modDt, qDept.modIp))
                .from(qDept)
                .leftJoin(qDealer).on(qDealer.dealerCd.eq(qDept.dealerCd))
                .where(qDept.delYn.eq(YNCode.N));


        ObjectMapper objectMapper= new ObjectMapper();

        if(!CustomUtils.isEmpty(deptSearchFilter)) {
            DeptSearchFilter deserializedDeptSearchFilter = objectMapper.readValue(deptSearchFilter, DeptSearchFilter.class);

            if(!CustomUtils.isEmpty(deserializedDeptSearchFilter.getDeptNm())) {
                query.where(qDept.deptNm.likeIgnoreCase("%" + deserializedDeptSearchFilter.getDeptNm() + "%"));
            }
            if(!CustomUtils.isEmpty(deserializedDeptSearchFilter.getDealerCd())) {
                query.where(qDealer.dealerCd.eq(Math.toIntExact(deserializedDeptSearchFilter.getDealerCd())));
            }

            if(!CustomUtils.isEmpty(deserializedDeptSearchFilter.getOnlySecondDepth())) {
                // 서브쿼리: deptIdx와 parentCd가 같은 dept의 parentCd 값을 가져옴
                JPQLQuery<Integer> subQuery = JPAExpressions.select(qDept.parentCd)
                        .from(qDept)
                        .where(qDept.deptIdx.eq(qDept.parentCd));

                query.where(qDept.parentCd.in(subQuery));
            }
        }

        if (!CustomUtils.isEmpty(dateRangeFilter)) {
            DateRangeFilter deserializedDateRangeFilter = (DateRangeFilter) objectMapper.readValue(dateRangeFilter, DateRangeFilter.class);
            if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getColumn())) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                if ("regDt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();

                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00");
                        booleanBuilder.and(qDept.regDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59");
                        booleanBuilder.and(qDept.regDt.before(endDate));
                    }

                    query.where(booleanBuilder);

                } else if ("modDt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();


                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                        booleanBuilder.and(qDept.modDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
                        booleanBuilder.and(qDept.modDt.before(endDate));
                    }

                    query.where(booleanBuilder);

                }else {
                    throw new IllegalStateException("유효하지 않은 Date range 검색 대상입니다.");
                }
            }
        }

        if(!CustomUtils.isEmpty(sorterValueFilter)) {
            SorterValueFilter deserializedSorterValueFilter = (SorterValueFilter) objectMapper.readValue(sorterValueFilter, SorterValueFilter.class);
            String sortedColumn = deserializedSorterValueFilter.getColumn();
            switch (sortedColumn) {
                case "deptIdx":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qDept.deptIdx.asc() : qDept.deptIdx.desc());
                    break;
                default:
                    query.orderBy(qDept.deptSort.asc());
            }
        } else {
            query.orderBy(qDept.deptSort.asc());
        }

        PaginationUtil paginationUtil = new PaginationUtil();
        return paginationUtil.applyPagination(query, pageNum, pageSize, skipPagination);
    }


    public List<DeptResDTO.IdxNm> findDeptIdxNmList(){

        QDept qDept = QDept.dept;

        JPQLQuery<DeptResDTO.IdxNm> query = jpaQueryFactory.select(new QDeptResDTO_IdxNm(qDept.deptIdx, qDept.deptNm))
                .from(qDept)
                .where(
                        qDept.delYn.eq(YNCode.N)
                );

        return query.fetch();
    }

    public DeptResDTO.DeptIdx updateDept(Dept dept, DeptReqDTO.CreateUpdateOne dto, String userId) {
        dept.updateDept(dto, userId);
        return new DeptResDTO.DeptIdx(dept);
    }


}
