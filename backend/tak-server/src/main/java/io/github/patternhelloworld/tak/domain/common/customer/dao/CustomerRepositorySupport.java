package io.github.patternhelloworld.tak.domain.common.customer.dao;

import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.common.domain.common.dto.DateRangeFilter;
import io.github.patternhelloworld.common.domain.common.dto.SorterValueFilter;
import io.github.patternhelloworld.common.util.CustomUtils;
import io.github.patternhelloworld.common.util.PaginationUtil;
import io.github.patternhelloworld.tak.config.database.CommonQuerydslRepositorySupport;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.customer.dto.CustomerCommonDTO;
import io.github.patternhelloworld.tak.domain.common.customer.dto.CustomerSearchFilter;

import io.github.patternhelloworld.tak.domain.common.customer.dto.QCustomerCommonDTO_One;
import io.github.patternhelloworld.tak.domain.common.customer.entity.Customer;
import io.github.patternhelloworld.tak.domain.common.customer.entity.QCustomer;
import io.github.patternhelloworld.tak.domain.common.customergroup.entity.QCustomerGroup;
import io.github.patternhelloworld.tak.domain.common.groupassign.dto.GroupAssignResDTO;
import io.github.patternhelloworld.tak.domain.common.groupassign.dto.GroupAssignSearchFilter;
import io.github.patternhelloworld.tak.domain.common.groupassign.dto.QGroupAssignResDTO_One;
import io.github.patternhelloworld.tak.domain.common.groupassign.entity.QGroupAssign;
import io.github.patternhelloworld.tak.domain.common.user.entity.QUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


@Repository
public class CustomerRepositorySupport extends CommonQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final CustomerRepository customerRepository;

    public CustomerRepositorySupport(CustomerRepository customerRepository, @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(Customer.class);
        this.customerRepository = customerRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Customer findById(Integer customerIdx) throws ResourceNotFoundException {
        return customerRepository.findById(customerIdx).orElseThrow(() -> new ResourceNotFoundException("findById - Code not found for this id :: " + customerIdx));
    }

    public Page<CustomerCommonDTO.One> findByPageAndFilter(Boolean skipPagination,
                                                           Integer pageNum,
                                                           Integer pageSize,
                                                           String customerSearchFilter,
                                                           String sorterValueFilter,
                                                           String dateRangeFilter) throws JsonProcessingException {

        QCustomer qCustomer = QCustomer.customer;
        QUser qUser = QUser.user;
        QCustomerGroup qCustomerGroup = QCustomerGroup.customerGroup;

        JPQLQuery<CustomerCommonDTO.One> query = jpaQueryFactory.select(new QCustomerCommonDTO_One(
                        qCustomer.customerIdx, qCustomer.customerName, qCustomer.customerType, qCustomer.customerInfo, qCustomer.purchasePlan,
                        qCustomer.ownerName, qCustomer.lastNameEn, qCustomer.firstNameEn, qCustomer.birthDate, qCustomer.nationality,
                        qCustomer.purchaseType, qCustomer.gender, qCustomer.email, qCustomer.tel, qCustomer.hp, qCustomer.fax,
                        qCustomer.otherContact, qCustomer.customerGroup.customerGroupIdx, qCustomer.customerGrade, qCustomer.zipcode,
                        qCustomer.addr1, qCustomer.addr2, qCustomer.addrSi, qCustomer.addrGugun, qCustomer.addrBname, qCustomer.specialNotes, qCustomer.companyName, qCustomer.codeGeneralPosition.codeCustomerIdx,
                        qCustomer.codeGeneralJob.codeCustomerIdx, qCustomer.smsSubscription, qCustomer.emailSubscription,
                        qCustomer.postalMailSubscription, qCustomer.codeGeneralContactMethod.codeCustomerIdx,
                        qCustomer.codeGeneralPurchaseDecisionFactor.codeCustomerIdx, qCustomer.codeGeneralInterestField.codeCustomerIdx,
                        qCustomer.companyAddress, qCustomer.personalDataConsent, qCustomer.personalDataConsentDate, qCustomer.chiefNm,
                        qCustomer.bizNo, qCustomer.corporationNo, qCustomer.uptae, qCustomer.upjong,
                        qUser.userIdx, qUser.name, qUser.regUserId,
                        qCustomer.regDt, qCustomer.regIp, qCustomer.modUserid, qCustomer.modDt, qCustomer.modIp, qCustomer.delUserid,
                        qCustomer.delDt, qCustomer.delIp, qCustomer.delYn
                ))
                .from(qCustomer)
                .leftJoin(qCustomer.userManager, qUser)
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
                query.where(qUser.outYn.eq(deserializedCustomerSearchFilter.getUserOutYn()));
            }

            if (!CustomUtils.isEmpty(deserializedCustomerSearchFilter.getDeptWithUser())) {
                Map<String, Object> deptWithUserMap = objectMapper.readValue(deserializedCustomerSearchFilter.getDeptWithUser(), Map.class);

                Integer deptIdx = Integer.valueOf(deptWithUserMap.get("deptIdx").toString());
                Long userIdx = deptWithUserMap.get("userIdx") != null ? Long.parseLong(deptWithUserMap.get("userIdx").toString()) : null;

                if (userIdx != null) {
                    // userIdx가 존재하면 regUserid (userId)로 검색
                    query.where(qCustomer.userManagerIdx.eq(userIdx));
                } else {
                    // userIdx가 없으면 deptIdx로 검색
                    query.where(qUser.deptIdx.eq(deptIdx));
                }
            }

            if (!CustomUtils.isEmpty(deserializedCustomerSearchFilter.getCustomerIdx())) {
                query.where(qCustomer.customerIdx.eq(deserializedCustomerSearchFilter.getCustomerIdx()));
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
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                        booleanBuilder.and(qCustomer.regDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
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

                } else if ("expectedPurchaseDt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();


                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                        booleanBuilder.and(qCustomer.expectedPurchaseDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
                        booleanBuilder.and(qCustomer.expectedPurchaseDt.before(endDate));
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

    public void deleteOne(Integer customerIdx, String modifier) {
        Customer customer = findById(customerIdx);
        customer.setDelDt(LocalDateTime.now());
        customer.setDelYn(YNCode.Y);
        customer.setDelUserid(modifier);
    }


    public void restoreOne(Integer customerIdx, String modifier) {
        Customer customer = findById(customerIdx);

        customer.setDelDt(null);
        customer.setDelYn(YNCode.N);
        customer.setDelUserid(modifier);
    }

    public void destroyOne(Integer customerIdx) {
        Customer customer = findById(customerIdx);
        customerRepository.delete(customer);
    }

    public Page<GroupAssignResDTO.One> findByPageAndFilterAndGroupAssign(Boolean skipPagination,
                                                           Integer pageNum,
                                                           Integer pageSize,
                                                           String groupAssignSearchFilter,
                                                           String sorterValueFilter,
                                                           String dateRangeFilter) throws JsonProcessingException {

        QCustomer qCustomer = QCustomer.customer;
        QUser qUser = QUser.user;
        QCustomerGroup qCustomerGroup = QCustomerGroup.customerGroup;
        QGroupAssign qGroupAssign = QGroupAssign.groupAssign;

        JPQLQuery<GroupAssignResDTO.One> query = jpaQueryFactory.select(new QGroupAssignResDTO_One(
                    qCustomer.customerIdx, qCustomer.customerName, qCustomer.customerType, qCustomer.customerInfo, qCustomer.birthDate, qCustomer.gender,
                    qCustomer.email, qCustomer.tel, qCustomer.hp, qCustomer.fax, qUser.userIdx, qUser.name, qGroupAssign.regUserid,
                    qGroupAssign.regDt, qGroupAssign.regIp, qGroupAssign.modUserid, qGroupAssign.modDt, qGroupAssign.modIp, qCustomerGroup.customerGroupIdx, qCustomerGroup.groupNm
                ))
                .from(qCustomer)
                .leftJoin(qGroupAssign).on(qCustomer.customerIdx.eq(qGroupAssign.customerIdx).and(qGroupAssign.delYn.eq(YNCode.N)))
                .leftJoin(qCustomerGroup).on(qGroupAssign.customerGroupIdx.eq(qCustomerGroup.customerGroupIdx).and(qCustomerGroup.delYn.eq(YNCode.N)))
                .leftJoin(qCustomer.userManager, qUser)
                .where(
                        qCustomer.delYn.eq(YNCode.N)
                );

        ObjectMapper objectMapper = new ObjectMapper();

        if (!CustomUtils.isEmpty(groupAssignSearchFilter)) {
            GroupAssignSearchFilter deserializedGroupAssignSearchFilte = objectMapper.readValue(groupAssignSearchFilter, GroupAssignSearchFilter.class);

            if(!CustomUtils.isEmpty(deserializedGroupAssignSearchFilte.getCustomerGroupIdx())) {
                if(deserializedGroupAssignSearchFilte.getCustomerGroupIdx() == null) { 
                    //그룹없음
                    query.where(qGroupAssign.customerGroupIdx.isNull());
                } else {
                    //나머지 그룹
                    query.where(qGroupAssign.customerGroupIdx.eq(deserializedGroupAssignSearchFilte.getCustomerGroupIdx()));
                }
            }

        }

        // Handle dateRangeFilter
        if (!CustomUtils.isEmpty(dateRangeFilter)) {
            DateRangeFilter deserializedDateRangeFilter = (DateRangeFilter) objectMapper.readValue(dateRangeFilter, DateRangeFilter.class);
        }

        // Handle sorterValueFilter
        if (!CustomUtils.isEmpty(sorterValueFilter)) {
            SorterValueFilter deserializedSorterValueFilter = objectMapper.readValue(sorterValueFilter, SorterValueFilter.class);
            String sortedColumn = deserializedSorterValueFilter.getColumn();
            switch (sortedColumn) {
                case "customerIdx":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCustomer.customerIdx.asc() : qCustomer.customerIdx.desc());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sorting column: " + sortedColumn);
            }
        }
        query.orderBy(qCustomer.customerIdx.desc());

        PaginationUtil paginationUtil = new PaginationUtil();
        return paginationUtil.applyPagination(query, pageNum, pageSize, skipPagination);
    }

}
