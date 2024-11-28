package io.github.patternhelloworld.tak.domain.common.relocatelog.dao;

import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.common.domain.common.dto.DateRangeFilter;
import io.github.patternhelloworld.common.domain.common.dto.SorterValueFilter;
import io.github.patternhelloworld.common.util.CustomUtils;
import io.github.patternhelloworld.common.util.PaginationUtil;
import io.github.patternhelloworld.tak.config.database.CommonQuerydslRepositorySupport;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.customer.dto.CustomerSearchFilter;
import io.github.patternhelloworld.tak.domain.common.customer.entity.QCustomer;
import io.github.patternhelloworld.tak.domain.common.customergroup.entity.QCustomerGroup;
import io.github.patternhelloworld.tak.domain.common.relocatelog.dto.QRelocateLogResDTO_One;
import io.github.patternhelloworld.tak.domain.common.relocatelog.dto.RelocateLogResDTO;
import io.github.patternhelloworld.tak.domain.common.relocatelog.entity.QRelocateLog;
import io.github.patternhelloworld.tak.domain.common.relocatelog.entity.RelocateLog;
import io.github.patternhelloworld.tak.domain.common.user.entity.QUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class RelocateLogRepositorySupport extends CommonQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final JdbcTemplate jdbcTemplate;
    private final RelocateLogRepository relocateLogRepository;

    public RelocateLogRepositorySupport(RelocateLogRepository relocateLogRepository, @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory, JdbcTemplate jdbcTemplate) {
        super(RelocateLog.class);
        this.relocateLogRepository = relocateLogRepository;
        this.jpaQueryFactory = jpaQueryFactory;
        this.jdbcTemplate = jdbcTemplate;
    }

    public RelocateLog findById(Integer relocateLogIdx) throws ResourceNotFoundException {
        return relocateLogRepository.findById(relocateLogIdx).orElseThrow(() -> new ResourceNotFoundException("findById - not found for this id :: " + relocateLogIdx));
    }
    public int createRelocateLog(Long userIdx, String userName, List<Object[]> fromUserManagers) {
        int[] result = jdbcTemplate.batchUpdate("INSERT INTO relocate_log (customer_idx, from_user_manager_idx, to_user_manager_idx, reg_userid) VALUES (?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object[] fromUserManager = fromUserManagers.get(i);

                ps.setInt(1, (Integer) fromUserManager[0]);
                ps.setLong(2, (Long) fromUserManager[1]);
                ps.setLong(3, userIdx);
                ps.setString(4, userName);
            }
            @Override
            public int getBatchSize() {
                return fromUserManagers.size();
            }
        });
        fromUserManagers.clear();
        return Arrays.stream(result).sum();
    }

    public Page<RelocateLogResDTO.One> findByPageAndFilter(Boolean skipPagination,
                                                           Integer pageNum,
                                                           Integer pageSize,
                                                           String customerSearchFilter,
                                                           String sorterValueFilter,
                                                           String dateRangeFilter) throws JsonProcessingException {

        QCustomer qCustomer = QCustomer.customer;
        QCustomerGroup qCustomerGroup = QCustomerGroup.customerGroup;
        QRelocateLog qRelocateLog = QRelocateLog.relocateLog;
        QUser qFromUser = new QUser("fromUser");
        QUser qToUser = new QUser("toUser");
        QUser qManagerUser = new QUser("managerUser");

        JPQLQuery<RelocateLogResDTO.One> query = jpaQueryFactory.select(new QRelocateLogResDTO_One(
                        qRelocateLog.relocateLogIdx, qCustomer.customerIdx, qCustomer.customerName, qFromUser.name, qToUser.name,
                        qRelocateLog.regUserid, qRelocateLog.regDt, qManagerUser.userIdx, qManagerUser.name, qCustomer.customerType,
                        qCustomer.customerInfo, qCustomer.purchasePlan, qCustomer.birthDate, qCustomer.email, qCustomer.tel, qCustomer.hp, qCustomer.fax,
                        qCustomer.otherContact, qCustomer.customerGroup.customerGroupIdx, qCustomer.customerGrade, qCustomer.zipcode, qCustomer.addr1, qCustomer.addr2,
                        qCustomer.smsSubscription, qCustomer.emailSubscription, qCustomer.postalMailSubscription, qCustomer.personalDataConsent,
                        qCustomer.personalDataConsentDate, qCustomer.regDt, qCustomer.regIp, qCustomer.delYn))
                .from(qRelocateLog)
                .leftJoin(qRelocateLog.customer, qCustomer)
                .leftJoin(qToUser).on(qRelocateLog.toUserManagerIdx.eq(qToUser.userIdx))
                .leftJoin(qFromUser).on(qRelocateLog.fromUserManagerIdx.eq(qFromUser.userIdx))
                .leftJoin(qCustomer.userManager, qManagerUser)
                .leftJoin(qCustomer.customerGroup, qCustomerGroup)
                .where(
                        qCustomer.delYn.eq(YNCode.N)
                );

        ObjectMapper objectMapper = new ObjectMapper();

        // Handle codeSearchFilter
        if (!CustomUtils.isEmpty(customerSearchFilter)) {
            CustomerSearchFilter deserializedCustomerSearchFilter = objectMapper.readValue(customerSearchFilter, CustomerSearchFilter.class);

            if (!CustomUtils.isEmpty(deserializedCustomerSearchFilter.getCustomerName())) {
                query.where(qCustomer.customerName.likeIgnoreCase("%" + deserializedCustomerSearchFilter.getCustomerName() + "%"));
            }

            if (!CustomUtils.isEmpty(deserializedCustomerSearchFilter.getCustomerType())) {
                query.where(qCustomer.customerType.eq(deserializedCustomerSearchFilter.getCustomerType()));
            }

            if (!CustomUtils.isEmpty(deserializedCustomerSearchFilter.getCustomerInfo())) {
                query.where(qCustomer.customerInfo.eq(deserializedCustomerSearchFilter.getCustomerInfo()));
            }

            if (!CustomUtils.isEmpty(deserializedCustomerSearchFilter.getCodeGeneralContactMethodIdx())) {
                query.where(qCustomer.codeGeneralContactMethodIdx.eq(deserializedCustomerSearchFilter.getCodeGeneralContactMethodIdx()));
            }

            if (!CustomUtils.isEmpty(deserializedCustomerSearchFilter.getHp())) {
                query.where(qCustomer.hp.likeIgnoreCase("%" + deserializedCustomerSearchFilter.getHp() + "%"));
            }

            if (!CustomUtils.isEmpty(deserializedCustomerSearchFilter.getTel())) {
                query.where(qCustomer.tel.likeIgnoreCase("%" + deserializedCustomerSearchFilter.getTel() + "%"));
            }

            if (!CustomUtils.isEmpty(deserializedCustomerSearchFilter.getGender())) {
                query.where(qCustomer.gender.eq(deserializedCustomerSearchFilter.getGender()));
            }

            if (!CustomUtils.isEmpty(deserializedCustomerSearchFilter.getRegUserId())) {
                query.where(qCustomer.regUserid.eq(deserializedCustomerSearchFilter.getRegUserId()));
            }

            if (!CustomUtils.isEmpty(deserializedCustomerSearchFilter.getPersonalDataConsent())) {
                query.where(qCustomer.personalDataConsent.eq(deserializedCustomerSearchFilter.getPersonalDataConsent()));
            }

            if (!CustomUtils.isEmpty(deserializedCustomerSearchFilter.getUserOutYn())) {
                query.where(qManagerUser.outYn.eq(deserializedCustomerSearchFilter.getUserOutYn()));
            }

            if (!CustomUtils.isEmpty(deserializedCustomerSearchFilter.getDeptWithUser())) {
                Map<String, Object> deptWithUserMap = objectMapper.readValue(deserializedCustomerSearchFilter.getDeptWithUser(), Map.class);

                Integer deptIdx = Integer.valueOf(deptWithUserMap.get("deptIdx").toString());
                String userId = deptWithUserMap.get("userId") != null ? deptWithUserMap.get("userId").toString() : null;

                if (userId != null) {
                    // userIdx가 존재하면 regUserid (userId)로 검색
                    query.where(qCustomer.regUserid.eq(userId));
                } else {
                    // userIdx가 없으면 deptIdx로 검색
                    query.where(qManagerUser.deptIdx.eq(deptIdx));
                }
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
                        booleanBuilder.and(qCustomer.regDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59");
                        booleanBuilder.and(qCustomer.regDt.before(endDate));
                    }

                    query.where(booleanBuilder);

                } else if ("modDt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();


                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                        booleanBuilder.and(qCustomer.modDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
                        booleanBuilder.and(qCustomer.modDt.before(endDate));
                    }

                    query.where(booleanBuilder);

                } else if ("birthDate".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();


                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDate startDate = LocalDate.parse(deserializedDateRangeFilter.getStartDate());
                        booleanBuilder.and(qCustomer.birthDate.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDate endDate = LocalDate.parse(deserializedDateRangeFilter.getEndDate());
                        booleanBuilder.and(qCustomer.birthDate.before(endDate));
                    }

                    query.where(booleanBuilder);

                } else {
                    throw new IllegalStateException("유효하지 않은 Date range 검색 대상입니다.");
                }
            }
        }

        // Handle sorterValueFilter
        if (!CustomUtils.isEmpty(sorterValueFilter)) {
            SorterValueFilter deserializedSorterValueFilter = objectMapper.readValue(sorterValueFilter, SorterValueFilter.class);
            String sortedColumn = deserializedSorterValueFilter.getColumn();
            switch (sortedColumn) {
                case "customerIdx":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCustomer.customerIdx.asc() : qCustomer.customerIdx.desc());
                    break;
                case "customerType":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCustomer.customerType.asc() : qCustomer.customerType.desc());
                    break;
                case "customerInfo":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCustomer.customerInfo.asc() : qCustomer.customerInfo.desc());
                    break;
                case "codeGeneralContactMethodIdx":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCustomer.codeGeneralContactMethodIdx.asc() : qCustomer.codeGeneralContactMethodIdx.desc());
                    break;
                case "gender":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCustomer.gender.asc() : qCustomer.gender.desc());
                    break;
                case "birthDate":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCustomer.birthDate.asc() : qCustomer.birthDate.desc());
                    break;
                case "customerName":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCustomer.customerName.asc() : qCustomer.customerName.desc());
                    break;
                case "regUserName":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCustomer.regUserid.asc() : qCustomer.regUserid.desc());
                    break;
                case "personalDataConsent":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCustomer.personalDataConsent.asc() : qCustomer.personalDataConsent.desc());
                    break;
                case "regDt":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCustomer.regDt.asc() : qCustomer.regDt.desc());
                    break;
                case "modDt":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCustomer.modDt.asc() : qCustomer.modDt.desc());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sorting column: " + sortedColumn);
            }
        }

        PaginationUtil paginationUtil = new PaginationUtil();
        return paginationUtil.applyPagination(query, pageNum, pageSize, skipPagination);
    }
}
