package io.github.patternhelloworld.tak.domain.crm.submenu.dao;

import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.common.domain.common.dto.DateRangeFilter;
import io.github.patternhelloworld.common.domain.common.dto.SorterValueFilter;
import io.github.patternhelloworld.common.util.CustomUtils;
import io.github.patternhelloworld.tak.config.database.CrmQuerydslRepositorySupport;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.crm.mainmenu.entity.QMainMenu;
import io.github.patternhelloworld.tak.domain.crm.submenu.dto.*;
import io.github.patternhelloworld.tak.domain.crm.submenu.entity.QSubMenu;
import io.github.patternhelloworld.tak.domain.crm.submenu.dto.SubMenuReqDto;
import io.github.patternhelloworld.tak.domain.crm.submenu.dto.SubMenuResDto;
import io.github.patternhelloworld.tak.domain.crm.submenu.dto.SubMenuSearchFilter;
import io.github.patternhelloworld.tak.domain.crm.submenu.entity.SubMenu;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class SubMenuRepositorySupport extends CrmQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final SubMenuRepository subMenuRepository;


    public SubMenuRepositorySupport(SubMenuRepository subMenuRepository, @Qualifier("crmJpaQueryFactory") JPAQueryFactory jpaQueryFactory)
    {
        super(SubMenu.class);

        this.jpaQueryFactory = jpaQueryFactory;
        this.subMenuRepository = subMenuRepository;
    }

    public SubMenu findById(Integer subMenuIdx) throws ResourceNotFoundException {
        return subMenuRepository.findById(subMenuIdx).orElseThrow(() -> new ResourceNotFoundException("다음 서브 메뉴를 찾을 수 없습니다. :: " + subMenuIdx));
    }


    public List<SubMenuResDto.OneWithMainMenu> getSubMenusPage(Integer pageNum,
                                                               Integer pageSize,
                                                               String subMenuSearchFilter,
                                                               String sorterValueFilter,
                                                               String dateRangeFilter) throws JsonProcessingException {




        final QSubMenu qSubMenu = QSubMenu.subMenu;
        final QMainMenu qMainMenu = QMainMenu.mainMenu;

        JPAQuery<SubMenuResDto.OneWithMainMenu> query = jpaQueryFactory
                .select(new QSubMenuResDto_OneWithMainMenu(
                        qSubMenu.subMenuIdx
                        , qSubMenu.mainMenuIdx
                        , qMainMenu.mainMenuNm
                        , qMainMenu.mainMenuKey
                        , qMainMenu.mainMenuPath
                        , qSubMenu.subMenuNm
                        , qSubMenu.subMenuKey
                        , qSubMenu.subMenuPath
                        , qSubMenu.subMenuGb
                        , qSubMenu.subMenuSort
                        , qSubMenu.dlrMenuAuthYn
                        , qSubMenu.regUserId
                        , qSubMenu.regDt
                        , qSubMenu.regIp
                        , qSubMenu.modUserId
                        , qSubMenu.modDt
                        , qSubMenu.modIp
                        , qSubMenu.delUserId
                        , qSubMenu.delDt
                        , qSubMenu.delIp
                        , qSubMenu.delYn
                ))
                .from(qSubMenu)
                .leftJoin(qSubMenu.mainMenu, qMainMenu);


        ObjectMapper objectMapper = new ObjectMapper();

        // Handle codeSearchFilter
        if (!CustomUtils.isEmpty(subMenuSearchFilter)) {
            SubMenuSearchFilter deserializedCustomerSearchFilter = objectMapper.readValue(subMenuSearchFilter, SubMenuSearchFilter.class);

            if (!CustomUtils.isEmpty(deserializedCustomerSearchFilter.getSubMenuNm())) {
                query.where(qSubMenu.subMenuNm.likeIgnoreCase("%" + deserializedCustomerSearchFilter.getSubMenuNm() + "%"));
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
                        booleanBuilder.and(qSubMenu.regDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59");
                        booleanBuilder.and(qSubMenu.regDt.before(endDate));
                    }

                    query.where(booleanBuilder);

                } else if ("modDt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();


                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        LocalDateTime startDate = LocalDateTime.parse(deserializedDateRangeFilter.getStartDate() + " 00:00:00", formatter);
                        booleanBuilder.and(qSubMenu.modDt.after(startDate));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        LocalDateTime endDate = LocalDateTime.parse(deserializedDateRangeFilter.getEndDate() + " 23:59:59", formatter);
                        booleanBuilder.and(qSubMenu.modDt.before(endDate));
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
                case "mainMenuNm":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qMainMenu.mainMenuNm.asc() : qMainMenu.mainMenuNm.desc());
                    break;
                case "subMenuNm":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qSubMenu.subMenuNm.asc() : qSubMenu.subMenuNm.desc());
                    break;
                case "subMenuSort":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qSubMenu.subMenuSort.asc() : qSubMenu.subMenuSort.desc());
                    break;
                case "modDt":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qSubMenu.modDt.asc() : qSubMenu.modDt.desc());
                    break;

                default:
                    throw new IllegalArgumentException("Invalid sorting column: " + sortedColumn);
            }
        }
        query.where(qSubMenu.delYn.eq(YNCode.N.getValue())).orderBy(qSubMenu.mainMenuIdx.asc(), qSubMenu.subMenuSort.asc());

        return query.fetch();
    }


    public List<SubMenuResDto.OneWithMainMenu> findListByFilter(@Nullable String subMenuSearchFilter) throws JsonProcessingException {

        final QSubMenu qSubMenu = QSubMenu.subMenu;
        final QMainMenu qMainMenu = QMainMenu.mainMenu;

        JPAQuery<SubMenuResDto.OneWithMainMenu> query = jpaQueryFactory
                .select(new QSubMenuResDto_OneWithMainMenu(
                        qSubMenu.subMenuIdx
                        , qSubMenu.mainMenuIdx
                        , qMainMenu.mainMenuNm
                        , qMainMenu.mainMenuKey
                        , qMainMenu.mainMenuPath
                        , qSubMenu.subMenuNm
                        , qSubMenu.subMenuKey
                        , qSubMenu.subMenuPath
                        , qSubMenu.subMenuGb
                        , qSubMenu.subMenuSort
                        , qSubMenu.dlrMenuAuthYn
                        , qSubMenu.regUserId
                        , qSubMenu.regDt
                        , qSubMenu.regIp
                        , qSubMenu.modUserId
                        , qSubMenu.modDt
                        , qSubMenu.modIp
                        , qSubMenu.delUserId
                        , qSubMenu.delDt
                        , qSubMenu.delIp
                        , qSubMenu.delYn
                ))
                .from(qSubMenu)
                .leftJoin(qSubMenu.mainMenu, qMainMenu);

        if (!CustomUtils.isEmpty(subMenuSearchFilter)) {

            ObjectMapper objectMapper = new ObjectMapper();

            SubMenuSearchFilter deserializedSubMenuSearchFilter = (SubMenuSearchFilter) objectMapper.readValue(subMenuSearchFilter,
                    SubMenuSearchFilter.class);
            if (!CustomUtils.isEmpty(deserializedSubMenuSearchFilter.getSubMenuNm())) {
                query.where(qSubMenu.subMenuNm.likeIgnoreCase("%" + deserializedSubMenuSearchFilter.getSubMenuNm() + "%"));
            }
            if (!CustomUtils.isEmpty(deserializedSubMenuSearchFilter.getGlobalField())) {
                query.where(qSubMenu.subMenuNm.likeIgnoreCase("%" + deserializedSubMenuSearchFilter.getGlobalField() + "%"));
            }
        }

        query.where(qSubMenu.delYn.eq(YNCode.N.getValue())).orderBy(qSubMenu.mainMenuIdx.asc(), qSubMenu.subMenuSort.asc());

        return query.fetch();
    }

    public SubMenuResDto.MinimalOne updateOne(Integer id, SubMenuReqDto.Update dto) {

        // 조회 순간 영속성 컨텍스트에 진입
        final SubMenu subMenu = subMenuRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 서브메뉴를 찾을 수 없습니다. ID : '" + id));

        subMenu.updateSubMenu(dto);
        // @Transactionl 안에서 종료가 된다면, 아래 .save 를 안해줘도 된다. 현재는 상위 함수인 Service 에 @Transactional 이 있어서 .save 를 안해주어도 됨.
        // subMenuRepository.save(subMenu);
        // [TO DO] 여기에 order 를 다른 row 들과 고려하는 로직을 넣어주세요.

        return new SubMenuResDto.MinimalOne(subMenu.getSubMenuIdx(),subMenu.getMainMenuIdx(),subMenu.getSubMenuNm(),subMenu.getSubMenuPath(),subMenu.getDelDt());
    }


}
