package com.autofocus.pms.hq.domain.common.extcode.dao;

import com.autofocus.pms.common.domain.common.dto.DateRangeFilter;
import com.autofocus.pms.common.domain.common.dto.SorterValueFilter;
import com.autofocus.pms.common.util.CustomUtils;
import com.autofocus.pms.common.util.PaginationUtil;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.extcode.dto.ExtCodeReqDTO;
import com.autofocus.pms.hq.domain.common.extcode.dto.ExtCodeResDTO;
import com.autofocus.pms.hq.domain.common.extcode.dto.ExtCodeSearchFilter;
import com.autofocus.pms.hq.domain.common.extcode.dto.QExtCodeResDTO_One;
import com.autofocus.pms.hq.domain.common.extcode.entity.ExtCode;
import com.autofocus.pms.hq.domain.common.extcode.entity.QExtCode;
import com.autofocus.pms.hq.domain.common.user.entity.User;
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

@Repository
public class ExtCodeRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;
    private final ExtCodeRepository extCodeRepository;


    public ExtCodeRepositorySupport(ExtCodeRepository extCodeRepository, @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory)
    {
        this.extCodeRepository = extCodeRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<ExtCodeResDTO.One> findByPageAndFilter(Boolean skipPagination,
                                                             Integer pageNum,
                                                             Integer pageSize,
                                                             String extCodeSearchFilter,
                                                             String sorterValueFilter,
                                                             String dateRangeFilter) throws JsonProcessingException {

        QExtCode qExtCode = QExtCode.extCode;

        JPQLQuery<ExtCodeResDTO.One> query = jpaQueryFactory.select(new QExtCodeResDTO_One(
                        qExtCode.extColorCodeIdx, qExtCode.code, qExtCode.description,
                        qExtCode.regUserId, qExtCode.regDt, qExtCode.modUserId, qExtCode.modDt))
                .from(qExtCode)
                .where(
                        qExtCode.delYn.eq(YNCode.N)
                );
        ObjectMapper objectMapper = new ObjectMapper();

        if(!CustomUtils.isEmpty(extCodeSearchFilter)) {
            ExtCodeSearchFilter deserializedExtCodeSearchFilter = objectMapper.readValue(extCodeSearchFilter, ExtCodeSearchFilter.class);

            if(!CustomUtils.isEmpty(deserializedExtCodeSearchFilter.getCode())) {
                query.where(qExtCode.code.likeIgnoreCase("%" + deserializedExtCodeSearchFilter.getCode() + "%"));
            }

            if(!CustomUtils.isEmpty(deserializedExtCodeSearchFilter.getDescription())) {
                query.where(qExtCode.description.likeIgnoreCase("%" + deserializedExtCodeSearchFilter.getDescription() + "%"));
            }
        }

        if(!CustomUtils.isEmpty(dateRangeFilter)) {
            DateRangeFilter deserializedDateRangeFilter = (DateRangeFilter) objectMapper.readValue(dateRangeFilter, DateRangeFilter.class);
            if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getColumn())) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                if ("regDt".equals(deserializedDateRangeFilter.getColumn())) {
                    BooleanBuilder booleanBuilder = new BooleanBuilder();

                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                        booleanBuilder.and(qExtCode.regDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
                        booleanBuilder.and(qExtCode.regDt.before(endDate));
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
                case "code":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExtCode.code.asc() : qExtCode.code.desc());
                    break;
                case "description":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExtCode.description.asc() : qExtCode.description.desc());
                    break;
                case "regDt":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExtCode.regDt.asc() : qExtCode.regDt.desc());
                    break;
                default:
                    throw new IllegalArgumentException("다음은 유효한 정렬 컬럼이 아닙니다 : " + sortedColumn);
            }
        }

        PaginationUtil paginationUtil = new PaginationUtil();
        return paginationUtil.applyPagination(query, pageNum, pageSize, skipPagination);
    }


    public ExtCodeResDTO.Idx updateExtCode(ExtCode extCode, ExtCodeReqDTO.CreateUpdateOne dto, User user) {
        extCode.updateExtCode(dto, user.getUserId());
        return new ExtCodeResDTO.Idx(extCode);
    }

    public void deleteExtCode(ExtCode extCode, String modifier) {
        extCode.setDelDt(LocalDateTime.now());
        extCode.setDelUserId(modifier);
        extCode.setDelYn(YNCode.Y);
    }
}
