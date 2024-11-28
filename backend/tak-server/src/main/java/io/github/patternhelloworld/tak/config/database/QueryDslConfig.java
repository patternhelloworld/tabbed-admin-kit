package io.github.patternhelloworld.tak.config.database;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class QueryDslConfig {


    @PersistenceContext(unitName = "commonEntityManager")
    private EntityManager commonEntityManager;
    @PersistenceContext(unitName = "crmEntityManager")
    private EntityManager crmEntityManager;

    @Bean
    @Primary
    @Qualifier("commonJpaQueryFactory")
    public JPAQueryFactory commonJpaQueryFactory() {
        return new JPAQueryFactory(commonEntityManager);
    }
    @Bean
    @Qualifier("crmJpaQueryFactory")
    public JPAQueryFactory crmJpaQueryFactory() {
        return new JPAQueryFactory(crmEntityManager);
    }

}
