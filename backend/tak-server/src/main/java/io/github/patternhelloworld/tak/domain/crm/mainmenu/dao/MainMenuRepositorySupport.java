package io.github.patternhelloworld.tak.domain.crm.mainmenu.dao;

import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.common.util.CustomUtils;
import io.github.patternhelloworld.tak.config.database.CrmQuerydslRepositorySupport;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.crm.mainmenu.dto.MainMenuReqDTO;
import io.github.patternhelloworld.tak.domain.crm.mainmenu.dto.MainMenuResDTO;
import io.github.patternhelloworld.tak.domain.crm.mainmenu.dto.MainMenuSearchFilter;

import io.github.patternhelloworld.tak.domain.crm.mainmenu.dto.QMainMenuResDTO_One;
import io.github.patternhelloworld.tak.domain.crm.mainmenu.entity.MainMenu;
import io.github.patternhelloworld.tak.domain.crm.mainmenu.entity.QMainMenu;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class MainMenuRepositorySupport extends CrmQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final MainMenuRepository mainMenuRepository;

    public MainMenuRepositorySupport(MainMenuRepository mainMenuRepository, @Qualifier("crmJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(MainMenu.class);

        this.jpaQueryFactory = jpaQueryFactory;
        this.mainMenuRepository = mainMenuRepository;
    }

    public List<MainMenuResDTO.One> findByAll(String mainMenuSearchFilter) throws JsonProcessingException {

        final QMainMenu qMainMenu = QMainMenu.mainMenu;

        JPAQuery<MainMenuResDTO.One> query = jpaQueryFactory
                .select(new QMainMenuResDTO_One(
                        qMainMenu.mainMenuIdx
                        , qMainMenu.mainMenuNm
                        , qMainMenu.mainMenuPath
                        , qMainMenu.isTable
                        , qMainMenu.mainMenuSort
                        , qMainMenu.mainMenuIcon
                        , qMainMenu.regUserid
                        , qMainMenu.regDt
                        , qMainMenu.regIp
                        , qMainMenu.modUserid
                        , qMainMenu.modDt
                        , qMainMenu.modIp
                ))
                .from(qMainMenu);

        ObjectMapper objectMapper = new ObjectMapper();

        if (!CustomUtils.isEmpty(mainMenuSearchFilter)) {
            MainMenuSearchFilter deserializedMainMenuSearchFilter = (MainMenuSearchFilter) objectMapper.readValue(mainMenuSearchFilter,
                    MainMenuSearchFilter.class);
            if (!CustomUtils.isEmpty(deserializedMainMenuSearchFilter.getMainMenuNm())) {
                query.where(qMainMenu.mainMenuNm.likeIgnoreCase("%" + deserializedMainMenuSearchFilter.getMainMenuNm() + "%"));
            }

            if (!CustomUtils.isEmpty(deserializedMainMenuSearchFilter.getGlobalField())) {
                query.where(qMainMenu.mainMenuNm.likeIgnoreCase("%" + deserializedMainMenuSearchFilter.getGlobalField() + "%"));
            }
        }

        query.where(qMainMenu.delYn.eq(YNCode.N.getValue())).orderBy(qMainMenu.mainMenuSort.asc());

        return query.fetch();
    }

    public MainMenu findByMainMenuIdx(Integer mainMenuIdx) throws ResourceNotFoundException {
        // ID 가 MainMenu 엔터티에 지정되어 있으므로 findById 로도 가능
        return mainMenuRepository.findById(mainMenuIdx).orElseThrow(() -> new ResourceNotFoundException("대메뉴 정보를 찾을 수 없습니다. ( id :: " + mainMenuIdx + ")"));
    }

    /*
    *
    *   @Transactional 안에 있다면, mainMenuRepository.save 를 안해줘도, 아래와 같이 엔터티에 값을 넣고 종료하는 것만으로도, @Transactional 종료 시점에
    *   DB에 persist 합니다.
    * */
    @Transactional(value = "crmTransactionManager")
    public MainMenuResDTO.One updateByMainMenuIdx(Integer mainMenuIdx, MainMenuReqDTO.UpdateOne dto) {
        MainMenu mainMenu = findByMainMenuIdx(mainMenuIdx);
        mainMenu.updateNameSort(dto);
        return new MainMenuResDTO.One(mainMenu);
    }

}
