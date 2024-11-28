package io.github.patternhelloworld.tak.domain.common.code.dao;

import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.common.domain.common.dto.DateRangeFilter;
import io.github.patternhelloworld.common.domain.common.dto.SorterValueFilter;
import io.github.patternhelloworld.common.util.CustomUtils;
import io.github.patternhelloworld.common.util.PaginationUtil;
import io.github.patternhelloworld.tak.config.database.CommonQuerydslRepositorySupport;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.code.dto.CodeCustomerReqDTO;
import io.github.patternhelloworld.tak.domain.common.code.dto.CodeCustomerResDTO;
import io.github.patternhelloworld.tak.domain.common.code.dto.CodeCustomerSearchFilter;
import io.github.patternhelloworld.tak.domain.common.code.dto.QCodeCustomerResDTO_One;
import io.github.patternhelloworld.tak.domain.common.code.entity.CodeCustomer;
import io.github.patternhelloworld.tak.domain.common.code.entity.QCodeCustomer;
import io.github.patternhelloworld.tak.domain.common.dealer.entity.Dealer;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Repository
public class CodeCustomerRepositorySupport extends CommonQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final CodeCustomerRepository codeCustomerRepository;

    public CodeCustomerRepositorySupport(CodeCustomerRepository codeCustomerRepository, @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(CodeCustomer.class);
        this.codeCustomerRepository = codeCustomerRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public CodeCustomer findById(Integer codeCustomerIdx) throws ResourceNotFoundException {
        return codeCustomerRepository.findById(codeCustomerIdx).orElseThrow(() -> new ResourceNotFoundException("findById - Code not found for this id :: " + codeCustomerIdx));
    }

    public CodeCustomer findByIdWithoutThrow(Integer codeCustomerIdx) throws ResourceNotFoundException {
        if(codeCustomerIdx == null){
            return null;
        }
        return codeCustomerRepository.findById(codeCustomerIdx).orElse(null);
    }

    public Page<CodeCustomerResDTO.One> findByPageAndFilter(Boolean skipPagination,
                                                            Integer pageNum,
                                                            Integer pageSize,
                                                            String codeCustomerSearchFilter,
                                                            String sorterValueFilter,
                                                            String dateRangeFilter) throws JsonProcessingException {

        QCodeCustomer qCodeCustomer = QCodeCustomer.codeCustomer;

        JPQLQuery<CodeCustomerResDTO.One> query = jpaQueryFactory.select(new QCodeCustomerResDTO_One(
                        qCodeCustomer.codeCustomerIdx, qCodeCustomer.categoryCd, qCodeCustomer.categoryNm, qCodeCustomer.categoryYn,
                        qCodeCustomer.parent, qCodeCustomer.codeCustomerIdx2, qCodeCustomer.codeCustomerNm, qCodeCustomer.sort,
                        qCodeCustomer.dealerCd, qCodeCustomer.regUserid, qCodeCustomer.regDt, qCodeCustomer.modUserid, qCodeCustomer.modDt))
                .from(qCodeCustomer)
                .where(
                        qCodeCustomer.delYn.eq(YNCode.N)
                );

        ObjectMapper objectMapper= new ObjectMapper();

        if(!CustomUtils.isEmpty(codeCustomerSearchFilter)) {
            CodeCustomerSearchFilter deserializedCodeCustomerSearchFilter = objectMapper.readValue(codeCustomerSearchFilter, CodeCustomerSearchFilter.class);

            if(!CustomUtils.isEmpty(deserializedCodeCustomerSearchFilter.getCodeNm())) {
                query.where(qCodeCustomer.codeCustomerNm.likeIgnoreCase("%" + deserializedCodeCustomerSearchFilter.getCodeNm() + "%"));
            }
            if(!CustomUtils.isEmpty(deserializedCodeCustomerSearchFilter.getCategoryYn())) {
                query.where(qCodeCustomer.categoryYn.eq(deserializedCodeCustomerSearchFilter.getCategoryYn()));
            }
            if(!CustomUtils.isEmpty(deserializedCodeCustomerSearchFilter.getCategoryNm())) {
                query.where(qCodeCustomer.categoryNm.likeIgnoreCase("%" + deserializedCodeCustomerSearchFilter.getCategoryNm() + "%"));
            }
        }

        // Handle dateRangeFilter
        if (!CustomUtils.isEmpty(dateRangeFilter)) {
            DateRangeFilter deserializedDateRangeFilter = (DateRangeFilter) objectMapper.readValue(dateRangeFilter, DateRangeFilter.class);
            if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getColumn())) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                if ("regDt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();

                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00");
                        booleanBuilder.and(qCodeCustomer.regDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59");
                        booleanBuilder.and(qCodeCustomer.regDt.before(endDate));
                    }

                    query.where(booleanBuilder);

                } else if ("modDt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();


                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                        booleanBuilder.and(qCodeCustomer.modDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
                        booleanBuilder.and(qCodeCustomer.modDt.before(endDate));
                    }

                    query.where(booleanBuilder);

                }  else {
                    throw new IllegalStateException("유효하지 않은 Date range 검색 대상입니다.");
                }
            }
        }

        if(!CustomUtils.isEmpty(sorterValueFilter)) {
            SorterValueFilter deserializedSorterValueFilter = (SorterValueFilter) objectMapper.readValue(sorterValueFilter, SorterValueFilter.class);
            String sortedColumn = deserializedSorterValueFilter.getColumn();
            switch (sortedColumn) {
                case "code_idx":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCodeCustomer.codeCustomerIdx.asc() : qCodeCustomer.codeCustomerIdx.desc());
                    break;
                default:
                    throw new IllegalArgumentException("다음은 유효한 정렬 컬럼이 아닙니다 : " + sortedColumn);
            }
        }

        PaginationUtil paginationUtil = new PaginationUtil();
        return paginationUtil.applyPagination(query, pageNum, pageSize, skipPagination);
    }

    public CodeCustomerResDTO.Idx updateCodeCustomer(CodeCustomer codeCustomer, CodeCustomerReqDTO.CreateUpdateOne dto, Dealer dealer, User user) {
        codeCustomer.updateCodeCustomer(dto, dealer, user.getUserId());
        return new CodeCustomerResDTO.Idx(codeCustomer);
    }

    public void deleteOne(CodeCustomer codeCustomer, String modifier, List<CodeCustomer> childCodeCustomers) {
        codeCustomer.setDelDt(LocalDateTime.now());
        codeCustomer.setDelUserid(modifier);
        codeCustomer.setDelYn(YNCode.Y);

        if(childCodeCustomers.size() > 0) {
            for (CodeCustomer child : childCodeCustomers) {
                child.setDelDt(LocalDateTime.now());
                child.setDelUserid(modifier);
                child.setDelYn(YNCode.Y);
            }
        }
    }

    public CodeCustomerResDTO.Idx updateMetaCustomerCode(CodeCustomer codeCustomer, CodeCustomerReqDTO.MetaCreateUpdateOne dto, User user) {
        codeCustomer.updateMetaCustomerCode(dto, user.getUserId());
        return new CodeCustomerResDTO.Idx(codeCustomer);
    }

}
