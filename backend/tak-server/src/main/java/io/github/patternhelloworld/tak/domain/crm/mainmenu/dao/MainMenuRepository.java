package io.github.patternhelloworld.tak.domain.crm.mainmenu.dao;

import io.github.patternhelloworld.tak.domain.crm.mainmenu.entity.MainMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

// 여기가 Integer 라면 Entity 의 pk 도 Integer 이어야 합니다.
public interface MainMenuRepository extends JpaRepository<MainMenu, Integer>, QuerydslPredicateExecutor<MainMenu> {
}
