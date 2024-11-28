package io.github.patternhelloworld.tak.domain.common.privacyagree.dao;

import io.github.patternhelloworld.tak.config.database.CommonQuerydslRepositorySupport;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.customergroup.dto.QCustomerGroupResDTO_One;
import io.github.patternhelloworld.tak.domain.common.customergroup.entity.QCustomerGroup;
import io.github.patternhelloworld.tak.domain.common.privacyagree.dto.PrivacyAgreeResDTO;
import io.github.patternhelloworld.tak.domain.common.privacyagree.dto.QPrivacyAgreeResDTO_One;
import io.github.patternhelloworld.tak.domain.common.privacyagree.entity.PrivacyAgree;
import io.github.patternhelloworld.tak.domain.common.privacyagree.entity.QPrivacyAgree;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

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
