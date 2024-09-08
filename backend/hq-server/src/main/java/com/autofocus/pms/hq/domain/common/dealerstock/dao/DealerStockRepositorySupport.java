package com.autofocus.pms.hq.domain.common.dealerstock.dao;

import com.autofocus.pms.common.config.response.error.exception.data.ResourceNotFoundException;
import com.autofocus.pms.common.domain.common.dto.DateRangeFilter;
import com.autofocus.pms.common.domain.common.dto.SorterValueFilter;
import com.autofocus.pms.common.util.CustomUtils;
import com.autofocus.pms.common.util.PaginationUtil;
import com.autofocus.pms.hq.config.database.CommonQuerydslRepositorySupport;
import com.autofocus.pms.hq.config.database.typeconverter.DealerStockUseType;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.carmaker.entity.QCarMaker;
import com.autofocus.pms.hq.domain.common.carmodel.entity.QCarModel;
import com.autofocus.pms.hq.domain.common.carmodeldetail.entity.QCarModelDetail;
import com.autofocus.pms.hq.domain.common.dealerstock.dto.DealerStockCommonDTO;
import com.autofocus.pms.hq.domain.common.dealerstock.dto.DealerStockSearchFilter;
import com.autofocus.pms.hq.domain.common.dealerstock.dto.QDealerStockCommonDTO_One;
import com.autofocus.pms.hq.domain.common.dealerstock.entity.DealerStock;
import com.autofocus.pms.hq.domain.common.dealerstock.entity.QDealerStock;
import com.autofocus.pms.hq.domain.common.dept.entity.QDept;
import com.autofocus.pms.hq.domain.common.stock.entity.QStock;
import com.autofocus.pms.hq.domain.common.vin.entity.QVin;
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
public class DealerStockRepositorySupport extends CommonQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final DealerStockRepository dealerStockRepository;

    public DealerStockRepositorySupport(DealerStockRepository dealerStockRepository, @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(DealerStock.class);
        this.dealerStockRepository = dealerStockRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public DealerStock findById(Long dealerStockIdx) throws ResourceNotFoundException {
        return dealerStockRepository.findById(dealerStockIdx).orElseThrow(() -> new ResourceNotFoundException("findById - Code not found for this id :: " + dealerStockIdx));
    }

    public Page<DealerStockCommonDTO.One> findByPageAndFilter(Boolean skipPagination,
                                                              Integer pageNum,
                                                              Integer pageSize,
                                                              String dealerStockSearchFilter,
                                                              String sorterValueFilter,
                                                              String dateRangeFilter) throws JsonProcessingException {

        QDealerStock qDealerStock = QDealerStock.dealerStock;

        // Many to One
        QStock qStock = QStock.stock;
        QVin qVin = QVin.vin;
        QCarModelDetail qCarModelDetail = QCarModelDetail.carModelDetail;
        QCarModel qCarModel = QCarModel.carModel;
        QCarMaker qCarMaker = QCarMaker.carMaker;

        // Many to One
        QDept qDept = QDept.dept;


        // plate number 는 미정
        JPQLQuery<DealerStockCommonDTO.One> query = jpaQueryFactory.select(new QDealerStockCommonDTO_One(
                        qDealerStock.dealerStockIdx, qDealerStock.useType, qDept.deptIdx, qDept.deptNm,

                        qCarModelDetail.year, qCarMaker.carMakerIdx,qCarMaker.carMakerNm,

                        qCarModel.carModelIdx, qCarModel.modelCode,
                        qCarModel.modelName, qCarModel.svcCode, qCarModel.svcName,

                        qCarModelDetail.carModelDetailIdx, qCarModelDetail.name, qCarModelDetail.code,
                        qCarModelDetail.motorType, qCarModelDetail.carName,

                        qStock.stockIdx,qVin.vinIdx, qVin.vinNumber, qDealerStock.importDate, qDealerStock.regUserid,
                        qDealerStock.regDt, qDealerStock.regIp, qDealerStock.modUserid, qDealerStock.modDt, qDealerStock.modIp, qDealerStock.delUserid,
                        qDealerStock.delDt, qDealerStock.delIp, qDealerStock.delYn
                ))
                .from(qDealerStock)
                // Many to One (stock -> vin -> carModelDetail -> carModel -> carMaker)
                .leftJoin(qDealerStock.stock, qStock)
                .leftJoin(qStock.vin, qVin)
                .leftJoin(qVin.carModelDetail, qCarModelDetail)
                .leftJoin(qCarModelDetail.carModel, qCarModel)
                .leftJoin(qCarModel.carMaker, qCarMaker)
                // Many to One
                .leftJoin(qDealerStock.dept, qDept)
                .where(
                        qDealerStock.delYn.eq(YNCode.N)
                );

        //  Many to One 만 있으므로 Group By 는 필요 없음.

        ObjectMapper objectMapper = new ObjectMapper();

        // Handle codeSearchFilter
        if (!CustomUtils.isEmpty(dealerStockSearchFilter)) {
            DealerStockSearchFilter deserializedDealerStockSearchFilter = objectMapper.readValue(dealerStockSearchFilter, DealerStockSearchFilter.class);

            if (!CustomUtils.isEmpty(deserializedDealerStockSearchFilter.getDealerStockIdx())) {
                query.where(qDealerStock.dealerStockIdx.eq(deserializedDealerStockSearchFilter.getDealerStockIdx()));
            }

            if (!CustomUtils.isEmpty(deserializedDealerStockSearchFilter.getUseType())
                    && !deserializedDealerStockSearchFilter.getUseType().equals(DealerStockUseType.UNDEFINED)) {
                query.where(qDealerStock.useType.eq(deserializedDealerStockSearchFilter.getUseType()));
            }

            if (!CustomUtils.isEmpty(deserializedDealerStockSearchFilter.getDeptIdx())) {
                query.where(qDealerStock.deptIdx.eq(deserializedDealerStockSearchFilter.getDeptIdx()));
            }

            if (!CustomUtils.isEmpty(deserializedDealerStockSearchFilter.getCarModelDetailYear())) {
                query.where(qCarModelDetail.year.eq(deserializedDealerStockSearchFilter.getCarModelDetailYear()));
            }

            if (!CustomUtils.isEmpty(deserializedDealerStockSearchFilter.getCarMakerIdx())) {
                query.where(qCarModel.carMakerIdx.eq(deserializedDealerStockSearchFilter.getCarMakerIdx()));
            }

            if (!CustomUtils.isEmpty(deserializedDealerStockSearchFilter.getCarModelIdx())) {
                query.where(qCarModel.carModelIdx.eq(deserializedDealerStockSearchFilter.getCarModelIdx()));
            }

            if (!CustomUtils.isEmpty(deserializedDealerStockSearchFilter.getCarModelDetailIdx())) {
                query.where(qCarModelDetail.carModelDetailIdx.eq(deserializedDealerStockSearchFilter.getCarModelDetailIdx()));
            }

            if (!CustomUtils.isEmpty(deserializedDealerStockSearchFilter.getVinNumber())) {
                query.where(qVin.vinNumber.likeIgnoreCase("%" + deserializedDealerStockSearchFilter.getVinNumber() + "%"));
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
                        booleanBuilder.and(qDealerStock.regDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59");
                        booleanBuilder.and(qDealerStock.regDt.before(endDate));
                    }

                    query.where(booleanBuilder);

                } else if ("modDt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();


                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                        booleanBuilder.and(qDealerStock.modDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
                        booleanBuilder.and(qDealerStock.modDt.before(endDate));
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
                case "dealerStockIdx":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qDealerStock.dealerStockIdx.asc() : qDealerStock.dealerStockIdx.desc());
                    break;
                case "deptNm":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qDept.deptNm.asc() : qDept.deptNm.desc());
                    break;
                case "carModelDetailYear":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCarModelDetail.year.asc() : qCarModelDetail.year.desc());
                    break;
                case "vinNumber":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qVin.vinNumber.asc() : qVin.vinNumber.desc());
                    break;
                case "carMakerIdx":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCarModel.carMakerIdx.asc() : qCarModel.carMakerIdx.desc());
                    break;
                case "carModelIdx":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCarModel.carModelIdx.asc() : qCarModel.carModelIdx.desc());
                    break;
                case "regDt":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qDealerStock.regDt.asc() : qDealerStock.regDt.desc());
                    break;
                case "modDt":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qDealerStock.modDt.asc() : qDealerStock.modDt.desc());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sorting column: " + sortedColumn);
            }
        }

        PaginationUtil paginationUtil = new PaginationUtil();
        return paginationUtil.applyPagination(query, pageNum, pageSize, skipPagination);
    }

    public void deleteOne(Long dealerStockIdx, String modifier) {
        DealerStock dealerStock = findById(dealerStockIdx);
        dealerStock.setDelDt(LocalDateTime.now());
        dealerStock.setDelYn(YNCode.Y);
        dealerStock.setDelUserid(modifier);
    }


    public void restoreOne(Long dealerStockIdx, String modifier) {
        DealerStock dealerStock = findById(dealerStockIdx);

        dealerStock.setDelDt(null);
        dealerStock.setDelYn(YNCode.N);
        dealerStock.setDelUserid(modifier);
    }

    public void destroyOne(Long dealerStockIdx) {
        DealerStock dealerStock = findById(dealerStockIdx);
        dealerStockRepository.delete(dealerStock);
    }


}
