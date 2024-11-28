package io.github.patternhelloworld.tak.domain.common.vin.dao;

import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.common.domain.common.dto.DateRangeFilter;
import io.github.patternhelloworld.common.domain.common.dto.SorterValueFilter;
import io.github.patternhelloworld.common.util.CustomUtils;
import io.github.patternhelloworld.common.util.PaginationUtil;
import io.github.patternhelloworld.tak.config.database.CommonQuerydslRepositorySupport;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.carmaker.entity.QCarMaker;
import io.github.patternhelloworld.tak.domain.common.carmodel.entity.QCarModel;
import io.github.patternhelloworld.tak.domain.common.carmodeldetail.entity.QCarModelDetail;
import io.github.patternhelloworld.tak.domain.common.vin.dto.QVinCommonDTO_One;
import io.github.patternhelloworld.tak.domain.common.vin.dto.VinCommonDTO;
import io.github.patternhelloworld.tak.domain.common.vin.dto.VinSearchFilter;
import io.github.patternhelloworld.tak.domain.common.vin.entity.QVin;
import io.github.patternhelloworld.tak.domain.common.vin.entity.Vin;
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
public class VinRepositorySupport extends CommonQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final VinRepository vinRepository;

    public VinRepositorySupport(VinRepository vinRepository, @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(Vin.class);
        this.vinRepository = vinRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Vin findById(Long vinIdx) throws ResourceNotFoundException {
        return vinRepository.findById(vinIdx).orElseThrow(() -> new ResourceNotFoundException("findById - Code not found for this id :: " + vinIdx));
    }

    public Page<VinCommonDTO.One> findByPageAndFilter(Boolean skipPagination,
                                                              Integer pageNum,
                                                              Integer pageSize,
                                                              String vinSearchFilter,
                                                              String sorterValueFilter,
                                                              String dateRangeFilter) throws JsonProcessingException {

        QVin qVin = QVin.vin;

        // Many to One
        QCarModelDetail qCarModelDetail = QCarModelDetail.carModelDetail;
        QCarModel qCarModel = QCarModel.carModel;
        QCarMaker qCarMaker = QCarMaker.carMaker;

        // plate number 는 미정
        JPQLQuery<VinCommonDTO.One> query = jpaQueryFactory.select(new QVinCommonDTO_One(
                        qVin.vinIdx,
                        qCarModelDetail.year, qCarMaker.carMakerIdx,qCarMaker.carMakerNm,

                        qCarModel.carModelIdx, qCarModel.modelCode,
                        qCarModel.modelName, qCarModel.svcCode, qCarModel.svcName,

                        qCarModelDetail.carModelDetailIdx, qCarModelDetail.name, qCarModelDetail.code,
                        qCarModelDetail.motorType, qCarModelDetail.carName,

                       qVin.vinNumber, qVin.regUserid,
                        qVin.regDt, qVin.regIp, qVin.modUserid, qVin.modDt, qVin.modIp, qVin.delUserid,
                        qVin.delDt, qVin.delIp, qVin.delYn
                ))
                .from(qVin)
                // Many to One (stock -> vin -> carModelDetail -> carModel -> carMaker)
                .leftJoin(qVin.carModelDetail, qCarModelDetail)
                .leftJoin(qCarModelDetail.carModel, qCarModel)
                .leftJoin(qCarModel.carMaker, qCarMaker)
                .where(
                        qVin.delYn.eq(YNCode.N)
                );

        //  Many to One 만 있으므로 Group By 는 필요 없음.

        ObjectMapper objectMapper = new ObjectMapper();

        // Handle codeSearchFilter
        if (!CustomUtils.isEmpty(vinSearchFilter)) {
            VinSearchFilter deserializedVinSearchFilter = objectMapper.readValue(vinSearchFilter, VinSearchFilter.class);

            if (!CustomUtils.isEmpty(deserializedVinSearchFilter.getVinIdx())) {
                query.where(qVin.vinIdx.eq(deserializedVinSearchFilter.getVinIdx()));
            }

            if (!CustomUtils.isEmpty(deserializedVinSearchFilter.getCarModelDetailYear())) {
                query.where(qCarModelDetail.year.eq(deserializedVinSearchFilter.getCarModelDetailYear()));
            }

            if (!CustomUtils.isEmpty(deserializedVinSearchFilter.getCarMakerIdx())) {
                query.where(qCarModel.carMakerIdx.eq(deserializedVinSearchFilter.getCarMakerIdx()));
            }

            if (!CustomUtils.isEmpty(deserializedVinSearchFilter.getCarModelIdx())) {
                query.where(qCarModel.carModelIdx.eq(deserializedVinSearchFilter.getCarModelIdx()));
            }

            if (!CustomUtils.isEmpty(deserializedVinSearchFilter.getCarModelDetailIdx())) {
                query.where(qCarModelDetail.carModelDetailIdx.eq(deserializedVinSearchFilter.getCarModelDetailIdx()));
            }

            if (!CustomUtils.isEmpty(deserializedVinSearchFilter.getVinNumber())) {
                query.where(qVin.vinNumber.likeIgnoreCase("%" + deserializedVinSearchFilter.getVinNumber() + "%"));
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
                        booleanBuilder.and(qVin.regDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59");
                        booleanBuilder.and(qVin.regDt.before(endDate));
                    }

                    query.where(booleanBuilder);

                } else if ("modDt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();


                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                        booleanBuilder.and(qVin.modDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
                        booleanBuilder.and(qVin.modDt.before(endDate));
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
                case "vinIdx":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qVin.vinIdx.asc() : qVin.vinIdx.desc());
                    break;
                case "carModelDetailYear":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCarModelDetail.year.asc() : qCarModelDetail.year.desc());
                    break;
                case "vinNumber":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qVin.vinNumber.asc() : qVin.vinNumber.desc());
                    break;
                case "carModelIdx":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qCarModel.carModelIdx.asc() : qCarModel.carModelIdx.desc());
                    break;
                case "regDt":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qVin.regDt.asc() : qVin.regDt.desc());
                    break;
                case "modDt":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qVin.modDt.asc() : qVin.modDt.desc());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sorting column: " + sortedColumn);
            }
        }

        PaginationUtil paginationUtil = new PaginationUtil();
        return paginationUtil.applyPagination(query, pageNum, pageSize, skipPagination);
    }

    public void deleteOne(Long vinIdx, String modifier) {
        Vin vin = findById(vinIdx);
        vin.setDelDt(LocalDateTime.now());
        vin.setDelYn(YNCode.Y);
        vin.setDelUserid(modifier);
    }


    public void restoreOne(Long vinIdx, String modifier) {
        Vin vin = findById(vinIdx);

        vin.setDelDt(null);
        vin.setDelYn(YNCode.N);
        vin.setDelUserid(modifier);
    }

    public void destroyOne(Long vinIdx) {
        Vin vin = findById(vinIdx);
        vinRepository.delete(vin);
    }


}
