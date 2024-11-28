package io.github.patternhelloworld.tak.config.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;


@Repository
public abstract class CrmQuerydslRepositorySupport extends QuerydslRepositorySupport {

    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     *
     * @param domainClass must not be {@literal null}.
     */
    public CrmQuerydslRepositorySupport(Class<?> domainClass) {
        super(domainClass);
    }

    @Override
    @PersistenceContext(unitName = "crmEntityManager")
    public void setEntityManager(@Qualifier("crmEntityManager") EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }
}
