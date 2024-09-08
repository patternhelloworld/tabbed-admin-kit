package com.autofocus.pms.hq.domain.crm.submenu.dao;

import com.autofocus.pms.hq.domain.crm.submenu.entity.SubMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SubMenuRepository extends JpaRepository<SubMenu, Integer>, QuerydslPredicateExecutor<SubMenu> {
}
