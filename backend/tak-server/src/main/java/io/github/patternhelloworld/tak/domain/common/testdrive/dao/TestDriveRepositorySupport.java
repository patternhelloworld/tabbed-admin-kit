package io.github.patternhelloworld.tak.domain.common.testdrive.dao;

import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.common.domain.common.dto.DateRangeFilter;
import io.github.patternhelloworld.common.util.CustomUtils;
import io.github.patternhelloworld.common.util.PaginationUtil;
import io.github.patternhelloworld.tak.config.database.CommonQuerydslRepositorySupport;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.customer.entity.QCustomer;
import io.github.patternhelloworld.tak.domain.common.dept.dao.DeptRepository;
import io.github.patternhelloworld.tak.domain.common.dept.entity.QDept;
import io.github.patternhelloworld.tak.domain.common.testdrive.dto.QTestDriveCommonDTO_One;
import io.github.patternhelloworld.tak.domain.common.testdrive.dto.TestDriveCommonDTO;
import io.github.patternhelloworld.tak.domain.common.testdrive.dto.TestDriveSearchFilter;
import io.github.patternhelloworld.tak.domain.common.testdrive.entity.QTestDrive;
import io.github.patternhelloworld.tak.domain.common.testdrive.entity.TestDrive;
import io.github.patternhelloworld.tak.domain.common.user.entity.QUser;
import io.github.patternhelloworld.tak.domain.common.vin.entity.QVin;
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
public class TestDriveRepositorySupport extends CommonQuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;
    private final TestDriveRepository testDriveRepository;
    private final DeptRepository deptRepository;

    public TestDriveRepositorySupport(TestDriveRepository testDriveRepository, DeptRepository deptRepository, @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(TestDrive.class);
        this.testDriveRepository = testDriveRepository;
        this.deptRepository = deptRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public TestDrive findById(Long testDriveIdx) throws ResourceNotFoundException {
        return testDriveRepository.findById(testDriveIdx)
                .orElseThrow(() -> new ResourceNotFoundException("TestDrive not found for this id: " + testDriveIdx));
    }

    public Page<TestDriveCommonDTO.One> findByPageAndFilter(Boolean skipPagination, Integer pageNum, Integer pageSize,
                                                            String testDriveSearchFilter, String sorterValueFilter,
                                                            String dateRangeFilter) throws JsonProcessingException {
        QTestDrive qTestDrive = QTestDrive.testDrive;

        QVin qVin = QVin.vin;
        QUser qUser = QUser.user;
        QCustomer qCustomer = QCustomer.customer;
        QDept qDept = QDept.dept;

        JPQLQuery<TestDriveCommonDTO.One> query = jpaQueryFactory.select(new QTestDriveCommonDTO_One(
                        qTestDrive.testDriveIdx, qVin.vinIdx, qVin.vinNumber, qDept.deptIdx, qDept.deptNm, qUser.userIdx, qUser.name,
                        qCustomer.customerIdx, qCustomer.customerName,
                        qTestDrive.carNo, qTestDrive.startDate, qTestDrive.startMile, qTestDrive.endDate,
                        qTestDrive.endMile, qTestDrive.testPlace, qTestDrive.remainFuel, qTestDrive.mileage,
                        qTestDrive.fuelFee, qTestDrive.washFee, qTestDrive.comment, qTestDrive.isApproved, qTestDrive.isDrive,
                        qTestDrive.regUserid, qTestDrive.regDt,  qTestDrive.modUserid,
                        qTestDrive.modDt, qTestDrive.delUserid, qTestDrive.delDt,
                        qTestDrive.delYn))

                .from(qTestDrive)
                .leftJoin(qTestDrive.vin, qVin)
                .leftJoin(qTestDrive.customer, qCustomer)
                .leftJoin(qTestDrive.user, qUser)
                .leftJoin(qUser.dept, qDept)
                .where(qTestDrive.delYn.eq(YNCode.N));

        ObjectMapper objectMapper = new ObjectMapper();

        if (!CustomUtils.isEmpty(testDriveSearchFilter)) {
            TestDriveSearchFilter deserializedTestDriveSearchFilter = objectMapper.readValue(testDriveSearchFilter, TestDriveSearchFilter.class);

            if (!CustomUtils.isEmpty(deserializedTestDriveSearchFilter.getVinNumber())) {
                query.where(qVin.vinNumber.likeIgnoreCase("%" + deserializedTestDriveSearchFilter.getVinNumber() + "%"));
            }

            if (!CustomUtils.isEmpty(deserializedTestDriveSearchFilter.getCarNo())) {
                query.where(qTestDrive.carNo.likeIgnoreCase("%" + deserializedTestDriveSearchFilter.getCarNo() + "%"));
            }

            if (!CustomUtils.isEmpty(deserializedTestDriveSearchFilter.getDeptIdx())) {

                // deserializedTestDriveSearchFilter.getDeptIdx() : 전시장, 그리고 그 아래의 dept 들을 검색해야 함...
                List<Integer> parentDeptIdxList = deptRepository.findChildDepts(deserializedTestDriveSearchFilter.getDeptIdx());
                query.where(qDept.deptIdx.in(parentDeptIdxList));

            }

        }

        if (!CustomUtils.isEmpty(dateRangeFilter)) {
            DateRangeFilter deserializedDateRangeFilter = (DateRangeFilter) objectMapper.readValue(dateRangeFilter, DateRangeFilter.class);
            if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getColumn())) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                if ("regDt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();

                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                        booleanBuilder.and(qTestDrive.regDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
                        booleanBuilder.and(qTestDrive.regDt.before(endDate));
                    }

                    query.where(booleanBuilder);

                } else if ("modDt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();


                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                        booleanBuilder.and(qTestDrive.modDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
                        booleanBuilder.and(qTestDrive.modDt.before(endDate));
                    }

                    query.where(booleanBuilder);

                } else if ("startEndDate".equals(deserializedDateRangeFilter.getColumn())
                        && !CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate()) && !CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();

                    LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                    LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);

                    booleanBuilder.and(qTestDrive.startDate.between(startDate, endDate).or(qTestDrive.endDate.between(startDate, endDate)));

                    query.where(booleanBuilder);

                } else {
                    throw new IllegalStateException("유효하지 않은 Date range 검색 대상입니다.");
                }
            }
        }

        PaginationUtil paginationUtil = new PaginationUtil();
        return paginationUtil.applyPagination(query, pageNum, pageSize, skipPagination);
    }

    public void deleteOne(Long testDriveIdx, String modifier) {
        TestDrive testDrive = findById(testDriveIdx);
        testDrive.setDelDt(LocalDateTime.now());
        testDrive.setDelYn(YNCode.Y);
        testDrive.setDelUserid(modifier);
    }

    public void restoreOne(Long testDriveIdx, String modifier) {
        TestDrive testDrive = findById(testDriveIdx);
        testDrive.setDelDt(null);
        testDrive.setDelYn(YNCode.N);
        testDrive.setDelUserid(modifier);
    }

    public void destroyOne(Long testDriveIdx) {
        TestDrive testDrive = findById(testDriveIdx);
        testDriveRepository.delete(testDrive);
    }
}
