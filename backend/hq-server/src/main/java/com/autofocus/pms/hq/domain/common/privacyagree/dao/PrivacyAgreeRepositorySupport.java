package com.autofocus.pms.hq.domain.common.privacyagree.dao;

import com.autofocus.pms.common.config.response.error.exception.data.ResourceNotFoundException;
import com.autofocus.pms.common.domain.common.dto.DateRangeFilter;
import com.autofocus.pms.common.domain.common.dto.SorterValueFilter;
import com.autofocus.pms.common.util.CustomUtils;
import com.autofocus.pms.common.util.PaginationUtil;
import com.autofocus.pms.hq.config.database.CommonQuerydslRepositorySupport;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.customergroup.dao.CustomerGroupRepository;
import com.autofocus.pms.hq.domain.common.customergroup.dto.CustomerGroupReqDTO;
import com.autofocus.pms.hq.domain.common.customergroup.dto.CustomerGroupResDTO;
import com.autofocus.pms.hq.domain.common.customergroup.dto.CustomerGroupSearchFilter;
import com.autofocus.pms.hq.domain.common.customergroup.dto.QCustomerGroupResDTO_One;
import com.autofocus.pms.hq.domain.common.customergroup.entity.CustomerGroup;
import com.autofocus.pms.hq.domain.common.customergroup.entity.QCustomerGroup;
import com.autofocus.pms.hq.domain.common.privacyagree.dto.PrivacyAgreeResDTO;
import com.autofocus.pms.hq.domain.common.privacyagree.dto.QPrivacyAgreeResDTO_One;
import com.autofocus.pms.hq.domain.common.privacyagree.entity.PrivacyAgree;
import com.autofocus.pms.hq.domain.common.privacyagree.entity.QPrivacyAgree;
import com.autofocus.pms.hq.domain.common.user.entity.User;
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
public class PrivacyAgreeRepositorySupport extends CommonQuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    private final PrivacyAgreeRepository privacyAgreeRepository;

    public PrivacyAgreeRepositorySupport(PrivacyAgreeRepository privacyAgreeRepository, @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(PrivacyAgree.class);
        this.privacyAgreeRepository = privacyAgreeRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public PrivacyAgreeResDTO.One findOneByCustomerIdx(Integer customerIdx) {
        QPrivacyAgree qPrivacyAgree = QPrivacyAgree.privacyAgree;

        JPQLQuery<PrivacyAgreeResDTO.One> query = jpaQueryFactory.select(new QPrivacyAgreeResDTO_One(
                qPrivacyAgree.privacyAgreeIdx, qPrivacyAgree.isAgree, qPrivacyAgree.fname, qPrivacyAgree.sname))
                .from(qPrivacyAgree)
                .where(
                    qPrivacyAgree.customerIdx.eq(customerIdx),
                    qPrivacyAgree.delYn.eq(YNCode.N)
                )
                .orderBy(qPrivacyAgree.customerIdx.desc())
                .limit(1);
        return query.fetchOne();
    }
}
