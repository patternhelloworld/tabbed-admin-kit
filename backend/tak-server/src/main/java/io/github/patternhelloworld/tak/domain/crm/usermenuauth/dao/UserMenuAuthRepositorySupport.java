package io.github.patternhelloworld.tak.domain.crm.usermenuauth.dao;

import io.github.patternhelloworld.tak.config.database.CrmQuerydslRepositorySupport;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.crm.mainmenu.entity.QMainMenu;
import io.github.patternhelloworld.tak.domain.crm.submenu.dao.SubMenuRepositorySupport;
import io.github.patternhelloworld.tak.domain.crm.submenu.dto.SubMenuResDto;
import io.github.patternhelloworld.tak.domain.crm.submenu.entity.QSubMenu;


import io.github.patternhelloworld.tak.domain.crm.usermenuauth.dto.QUserMenuAuthCommonDTO_OneWithSubMenu;
import io.github.patternhelloworld.tak.domain.crm.usermenuauth.dto.QUserMenuAuthCommonDTO_Permission;
import io.github.patternhelloworld.tak.domain.crm.usermenuauth.dto.UserMenuAuthCommonDTO;
import io.github.patternhelloworld.tak.domain.crm.usermenuauth.entity.QUserMenuAuth;
import io.github.patternhelloworld.tak.domain.crm.usermenuauth.entity.UserMenuAuth;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserMenuAuthRepositorySupport extends CrmQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final UserMenuAuthRepository userMenuAuthRepository;
    private final SubMenuRepositorySupport subMenuRepositorySupport;

    public UserMenuAuthRepositorySupport(UserMenuAuthRepository userMenuAuthRepository,
                                         SubMenuRepositorySupport subMenuRepositorySupport,
                                         @Qualifier("crmJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(UserMenuAuth.class);

        this.userMenuAuthRepository = userMenuAuthRepository;
        this.subMenuRepositorySupport = subMenuRepositorySupport;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<UserMenuAuthCommonDTO.OneWithSubMenu> findListByUserIdxAndSyncEmptyPermissions(Long userIdx) throws JsonProcessingException {

        QUserMenuAuth qUserMenuAuth = QUserMenuAuth.userMenuAuth;
        QSubMenu qSubMenu = QSubMenu.subMenu;
        QMainMenu qMainMenu = QMainMenu.mainMenu;


        List<SubMenuResDto.OneWithMainMenu> subMenus = subMenuRepositorySupport.findListByFilter(null);

        JPAQuery<UserMenuAuthCommonDTO.OneWithSubMenu> query = jpaQueryFactory
                .select(new QUserMenuAuthCommonDTO_OneWithSubMenu(
                        qUserMenuAuth.userMenuAuthIdx,
                        qUserMenuAuth.ynLst,
                        qUserMenuAuth.ynInt,
                        qUserMenuAuth.ynMod,
                        qUserMenuAuth.ynDel,
                        qUserMenuAuth.ynXls,
                        qSubMenu.subMenuIdx,
                        qSubMenu.subMenuNm,
                        qMainMenu.mainMenuIdx,
                        qMainMenu.mainMenuNm,
                        qUserMenuAuth.userIdx,
                        qUserMenuAuth.userId,
                        qUserMenuAuth.reason,
                        qUserMenuAuth.dealerCd,
                        qUserMenuAuth.regUserId,
                        qUserMenuAuth.regDt,
                        qUserMenuAuth.regIp,
                        qUserMenuAuth.modUserId,
                        qUserMenuAuth.modDt,
                        qUserMenuAuth.modIp,
                        qUserMenuAuth.delUserId,
                        qUserMenuAuth.delDt,
                        qUserMenuAuth.delIp,
                        qUserMenuAuth.delYn))
                .from(qUserMenuAuth)
                .leftJoin(qUserMenuAuth.subMenu, qSubMenu)
                .leftJoin(qSubMenu.mainMenu, qMainMenu)
                .where(qUserMenuAuth.userIdx.eq(userIdx))
                .where(qUserMenuAuth.delDt.isNull());

        // sub menu 개수가 반영되지 않은 것. 예를 들어 1개만 나올 수도 있다.
        List<UserMenuAuthCommonDTO.OneWithSubMenu> rawPermissions = query.fetch();

        // 프론트로 sub menu 의 개수만큼 리턴해야 한다. 아래 orElse 는 user_menu_auth 에 아예 값이 없는 경우의 디폴트를 리턴한다.
        List<UserMenuAuthCommonDTO.OneWithSubMenu> emptyPKReflectedOneWithSubMenus = subMenus.stream().map(subMenu -> rawPermissions.stream()
                    .filter(oneWithSubMenu -> oneWithSubMenu.getSubMenuIdx().equals(subMenu.getSubMenuIdx()))
                    .findAny()
                    .orElse(UserMenuAuthCommonDTO.OneWithSubMenu.builder()
                            .subMenuNm(subMenu.getSubMenuNm()).subMenuIdx(subMenu.getSubMenuIdx())
                            .userIdx(userIdx)
                            .ynDel(YNCode.N).ynInt(YNCode.N).ynLst(YNCode.N).ynMod(YNCode.N).ynXls(YNCode.N)
                            .build())).toList();

        // sub menu 의 갯수만큼 항상 있도록 유지
        // TO DO. 어쟀거나 GET API 에서 발생한 Insert, Update 이므로 리팩토링이 필요하다면 고민 필요.
        userMenuAuthRepository.saveAll(emptyPKReflectedOneWithSubMenus.stream().map(x -> x.toEntity(subMenuRepositorySupport.findById(x.getSubMenuIdx()))).toList());

        // 상기 saveAll 한 것을 반영한 결과를 가져온다.
        return query.fetch();

    }

    public List<UserMenuAuthCommonDTO.Permission> findList(Long userIdx) {

        QUserMenuAuth qUserMenuAuth = QUserMenuAuth.userMenuAuth;
        QSubMenu qSubMenu = QSubMenu.subMenu;
        QMainMenu qMainMenu = QMainMenu.mainMenu;

        JPAQuery<UserMenuAuthCommonDTO.Permission> query = jpaQueryFactory
                .select(new QUserMenuAuthCommonDTO_Permission(
                        qUserMenuAuth.ynLst,
                        qUserMenuAuth.ynInt,
                        qUserMenuAuth.ynMod,
                        qUserMenuAuth.ynDel,
                        qUserMenuAuth.ynXls,
                        qSubMenu.subMenuNm,
                        qSubMenu.subMenuPath,
                        qSubMenu.subMenuKey,
                        qMainMenu.mainMenuNm,
                        qMainMenu.mainMenuPath,
                        qMainMenu.mainMenuKey))
                .from(qUserMenuAuth)
                .leftJoin(qUserMenuAuth.subMenu, qSubMenu)
                .leftJoin(qSubMenu.mainMenu, qMainMenu)
                .where(qUserMenuAuth.userIdx.eq(userIdx))
                .where(qUserMenuAuth.delDt.isNull());

        // 상기 saveAll 한 것을 반영한 결과를 가져온다.
        return query.fetch();

    }



    @Transactional(value = "crmTransactionManager", rollbackFor=Exception.class)
    public List<UserMenuAuthCommonDTO.OneWithSubMenu> updateOneWithSubMenuList(List<UserMenuAuthCommonDTO.OneWithSubMenu> dtos, String modifier){

        List<UserMenuAuthCommonDTO.OneWithSubMenu> permissions = new ArrayList<>();

        for (UserMenuAuthCommonDTO.OneWithSubMenu dto : dtos) {
            Optional<UserMenuAuth> optionalUserMenuAuth = userMenuAuthRepository.findByUserMenuAuthIdxAndDelDt(dto.getUserMenuAuthIdx(), null);

            if (optionalUserMenuAuth.isPresent()) {
                UserMenuAuth userMenuAuth = optionalUserMenuAuth.get();
                userMenuAuth.setYnLst(dto.getYnLst());
                userMenuAuth.setYnInt(dto.getYnInt());
                userMenuAuth.setYnMod(dto.getYnMod());
                userMenuAuth.setYnDel(dto.getYnDel());
                userMenuAuth.setYnXls(dto.getYnXls());
                userMenuAuth.setUserIdx(dto.getUserIdx());
                userMenuAuth.setUserId(dto.getUserId());
                userMenuAuth.setReason(dto.getReason());
                userMenuAuth.setDealerCd(dto.getDealerCd());
                userMenuAuth.setRegUserId(dto.getRegUserId());

                if(userMenuAuth.getRegDt() == null){
                    userMenuAuth.setRegDt(LocalDateTime.now());
                }else{
                    userMenuAuth.setRegDt(dto.getRegDt());
                }
                if(userMenuAuth.getRegUserId() == null){
                    userMenuAuth.setRegUserId(modifier);
                }else{
                    userMenuAuth.setRegUserId(dto.getModUserId());
                }

                userMenuAuth.setRegIp(dto.getRegIp());

                dto.setModUserId(modifier);
                userMenuAuth.setModUserId(modifier);

                dto.setModDt(LocalDateTime.now());
                userMenuAuth.setModDt(dto.getModDt());

                userMenuAuth.setModIp(dto.getModIp());
                userMenuAuth.setDelUserId(dto.getDelUserId());
                userMenuAuth.setDelDt(dto.getDelDt());
                userMenuAuth.setDelIp(dto.getDelIp());
                userMenuAuth.setDelYn(dto.getDelYn());

                userMenuAuthRepository.save(userMenuAuth);

                permissions.add(dto);
            }


        }

        return permissions;
    }

}
