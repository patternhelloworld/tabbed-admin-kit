package com.autofocus.pms.hq.domain.common.groupassign.dao;

import com.autofocus.pms.hq.config.database.CommonQuerydslRepositorySupport;
import com.autofocus.pms.hq.domain.common.groupassign.entity.GroupAssign;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;


@Repository
public class GroupAssignRepositorySupport extends CommonQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final GroupAssignRepository groupAssignRepository;

    public GroupAssignRepositorySupport(GroupAssignRepository groupAssignRepository, @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(GroupAssign.class);
        this.groupAssignRepository = groupAssignRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }
}
