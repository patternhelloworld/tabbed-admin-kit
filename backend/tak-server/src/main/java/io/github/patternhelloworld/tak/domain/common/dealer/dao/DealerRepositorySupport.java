package io.github.patternhelloworld.tak.domain.common.dealer.dao;

import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.common.domain.common.dto.DateRangeFilter;
import io.github.patternhelloworld.common.domain.common.dto.SorterValueFilter;
import io.github.patternhelloworld.common.util.CustomUtils;
import io.github.patternhelloworld.common.util.PaginationUtil;
import io.github.patternhelloworld.tak.config.database.CommonQuerydslRepositorySupport;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.dealer.dto.*;
import io.github.patternhelloworld.tak.domain.common.dealer.dto.DealerReqDTO;
import io.github.patternhelloworld.tak.domain.common.dealer.dto.DealerResDTO;
import io.github.patternhelloworld.tak.domain.common.dealer.dto.DealerSearchFilter;
import io.github.patternhelloworld.tak.domain.common.dealer.entity.Dealer;
import io.github.patternhelloworld.tak.domain.common.dealer.entity.QDealer;
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
public class DealerRepositorySupport extends CommonQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final DealerRepository dealerRepository;

    public DealerRepositorySupport(DealerRepository dealerRepository, @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(Dealer.class);
        this.dealerRepository = dealerRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }


    public Dealer findById(Integer id) throws ResourceNotFoundException {
        return dealerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("findById - Dealer not found for this id :: " + id));
    }


    public Page<DealerResDTO.One> findByPageAndFilter(Boolean skipPagination,
                                                      Integer pageNum,
                                                      Integer pageSize,
                                                      String dealerSearchFilter,
                                                      String sorterValueFilter,
                                                      String dateRangeFilter) throws JsonProcessingException {
        QDealer qDealer = QDealer.dealer;

        JPQLQuery<DealerResDTO.One> query = jpaQueryFactory.select(new QDealerResDTO_One(qDealer.dealerCd, qDealer.dealerGb, qDealer.dealerNm, qDealer.shortNm,
                        qDealer.isMain, qDealer.hphone, qDealer.tel, qDealer.fax, qDealer.chiefNm, qDealer.email, qDealer.uptae, qDealer.upjong,
                        qDealer.zipcode, qDealer.addr1, qDealer.addr2, qDealer.pdiRequestCount, qDealer.regUserid, qDealer.regDt, qDealer.regIp,
                        qDealer.modUserid, qDealer.modDt, qDealer.modIp))
                .from(qDealer)
                .where(
                        qDealer.delYn.eq(YNCode.N)
                );


        ObjectMapper objectMapper= new ObjectMapper();

        if(!CustomUtils.isEmpty(dealerSearchFilter)) {
            DealerSearchFilter deserializedDealerSearchFilter = objectMapper.readValue(dealerSearchFilter, DealerSearchFilter.class);

            if(!CustomUtils.isEmpty(deserializedDealerSearchFilter.getDealerNm())) {
                query.where(qDealer.dealerNm.likeIgnoreCase("%" + deserializedDealerSearchFilter.getDealerNm() + "%"));
            }
            if(!CustomUtils.isEmpty(deserializedDealerSearchFilter.getIsMain())) {
                query.where(qDealer.isMain.eq(deserializedDealerSearchFilter.getIsMain()));
            }
            if(!CustomUtils.isEmpty(deserializedDealerSearchFilter.getShortNm())) {
                query.where(qDealer.shortNm.likeIgnoreCase("%" + deserializedDealerSearchFilter.getShortNm() + "%"));
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
                        booleanBuilder.and(qDealer.regDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59");
                        booleanBuilder.and(qDealer.regDt.before(endDate));
                    }

                    query.where(booleanBuilder);

                } else if ("modDt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();


                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                        booleanBuilder.and(qDealer.modDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
                        booleanBuilder.and(qDealer.modDt.before(endDate));
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
                case "dealerCd":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qDealer.dealerCd.asc() : qDealer.dealerCd.desc());
                    break;
                case "isMain":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qDealer.isMain.asc() : qDealer.isMain.desc());
                    break;
                case "shortNm":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qDealer.shortNm.asc() : qDealer.shortNm.desc());
                    break;
                default:
                    throw new IllegalArgumentException("다음은 유효한 정렬 컬럼이 아닙니다 : " + sortedColumn);
            }
        }

        PaginationUtil paginationUtil = new PaginationUtil();
        return paginationUtil.applyPagination(query, pageNum, pageSize, skipPagination);
    }

    public List<DealerResDTO.CdNm> findDealerCdNmList(){
        QDealer qDealer = QDealer.dealer;

        JPQLQuery<DealerResDTO.CdNm> query = jpaQueryFactory.select(new QDealerResDTO_CdNm(qDealer.dealerCd, qDealer.dealerNm))
                .from(qDealer)
                .where(
                        qDealer.delYn.eq(YNCode.N)
                );

        return query.fetch();
    }

    public DealerResDTO.DealerCd updateDealer(Dealer dealer, DealerReqDTO.CreateUpdateOne dto) {
        dealer.updateDealer(dto);
        return new DealerResDTO.DealerCd(dealer);
    }



}
