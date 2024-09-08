package com.autofocus.pms.hq.domain.common.customergroup.dao;

import com.autofocus.pms.common.config.response.error.exception.data.ResourceNotFoundException;
import com.autofocus.pms.common.domain.common.dto.DateRangeFilter;
import com.autofocus.pms.common.domain.common.dto.SorterValueFilter;
import com.autofocus.pms.common.util.CustomUtils;
import com.autofocus.pms.common.util.PaginationUtil;
import com.autofocus.pms.hq.config.database.CommonQuerydslRepositorySupport;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.customergroup.dto.CustomerGroupReqDTO;
import com.autofocus.pms.hq.domain.common.customergroup.dto.CustomerGroupResDTO;
import com.autofocus.pms.hq.domain.common.customergroup.dto.CustomerGroupSearchFilter;
import com.autofocus.pms.hq.domain.common.customergroup.dto.QCustomerGroupResDTO_One;
import com.autofocus.pms.hq.domain.common.customergroup.entity.CustomerGroup;
import com.autofocus.pms.hq.domain.common.customergroup.entity.QCustomerGroup;
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
public class CustomerGroupRepositorySupport extends CommonQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final CustomerGroupRepository customerGroupRepository;

    public CustomerGroupRepositorySupport(CustomerGroupRepository customerGroupRepository, @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(CustomerGroup.class);
        this.customerGroupRepository = customerGroupRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public CustomerGroup findById(Integer customerGroupIdx) throws ResourceNotFoundException {
        return customerGroupRepository.findById(customerGroupIdx).orElseThrow(() -> new ResourceNotFoundException("findById - not found for this id :: " + customerGroupIdx));
    }

    public CustomerGroup findByIdWithoutThrow(Integer customerGroupIdx) {
        if(customerGroupIdx == null){
            return null;
        }
        return customerGroupRepository.findById(customerGroupIdx).orElse(null);
    }

    public Page<CustomerGroupResDTO.One> findByPageAndFilter(Boolean skipPagination,
                                    Integer pageNum,
                                    Integer pageSize,
                                    String customerGroupSearchFilter,
                                    String sorterValueFilter,
                                    String dateRangeFilter) throws JsonProcessingException {

        QCustomerGroup qCustomerGroup = QCustomerGroup.customerGroup;

        JPQLQuery<CustomerGroupResDTO.One> query = jpaQueryFactory.select(new QCustomerGroupResDTO_One(
                qCustomerGroup.customerGroupIdx, qCustomerGroup.dealerCd, qCustomerGroup.userid, qCustomerGroup.groupNm,
                qCustomerGroup.regUserid, qCustomerGroup.regDt, qCustomerGroup.modUserid, qCustomerGroup.modDt))
                .from(qCustomerGroup)
                .where(
                    qCustomerGroup.delYn.eq(YNCode.N)
                );
        ObjectMapper objectMapper = new ObjectMapper();

        if(!CustomUtils.isEmpty(customerGroupSearchFilter)) {
            CustomerGroupSearchFilter deserializedCustomerGroupSearchFilter = objectMapper.readValue(customerGroupSearchFilter, CustomerGroupSearchFilter.class);

            if(!CustomUtils.isEmpty(deserializedCustomerGroupSearchFilter.getGroupNm())) {
                query.where(qCustomerGroup.groupNm.likeIgnoreCase("%" + deserializedCustomerGroupSearchFilter.getGroupNm() + "%"));
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
                        booleanBuilder.and(qCustomerGroup.regDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
                        booleanBuilder.and(qCustomerGroup.regDt.before(endDate));
                    }

                    query.where(booleanBuilder);

                }else if ("modDt".equals(deserializedDateRangeFilter.getColumn())) {
                    BooleanBuilder booleanBuilder = new BooleanBuilder();

                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                        booleanBuilder.and(qCustomerGroup.modDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
                        booleanBuilder.and(qCustomerGroup.modDt.before(endDate));
                    }

                    query.where(booleanBuilder);

                }else if ("delDt".equals(deserializedDateRangeFilter.getColumn())) {
                    BooleanBuilder booleanBuilder = new BooleanBuilder();

                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                        booleanBuilder.and(qCustomerGroup.delDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
                        booleanBuilder.and(qCustomerGroup.delDt.before(endDate));
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
                case "customerGroupIdx":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCustomerGroup.customerGroupIdx.asc() : qCustomerGroup.customerGroupIdx.desc());
                    break;
                default:
                    throw new IllegalArgumentException("다음은 유효한 정렬 컬럼이 아닙니다 : " + sortedColumn);
            }
        }

        PaginationUtil paginationUtil = new PaginationUtil();
        return paginationUtil.applyPagination(query, pageNum, pageSize, skipPagination);
    }

    public CustomerGroupResDTO.Idx updateCustomerGroup(CustomerGroup customerGroup, CustomerGroupReqDTO.CreateUpdateOne dto, User user) {
        customerGroup.updateCustomerGroup(dto, user.getUserId());
        return new CustomerGroupResDTO.Idx(customerGroup);
    }

    public void deleteCustomerGroup(CustomerGroup customerGroup, String modifier) {
        customerGroup.setDelDt(LocalDateTime.now());
        customerGroup.setDelUserid(modifier);
        customerGroup.setDelYn(YNCode.Y);
    }
}
