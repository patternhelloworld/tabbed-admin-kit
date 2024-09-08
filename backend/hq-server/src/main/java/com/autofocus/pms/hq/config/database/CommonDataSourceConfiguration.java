package com.autofocus.pms.hq.config.database;

import com.autofocus.pms.hq.config.database.typeconverter.CustomerGrade;
import com.autofocus.pms.hq.config.database.typeconverter.mybatis.JsonTypeHandler;
import com.autofocus.pms.hq.config.database.typeconverter.ManagementDepartment;
import com.autofocus.pms.hq.config.database.typeconverter.ViewPermission;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.autofocus.pms.hq.mapper"}, sqlSessionFactoryRef = "commonSqlSessionFactory")
@EnableJpaRepositories(
        basePackages = {"com.autofocus.pms.hq.domain.common", "com.autofocus.pms.hq.config.securityimpl", "com.autofocus.pms.security.oauth2"},
        entityManagerFactoryRef = "commonEntityManagerFactory",
        transactionManagerRef= "commonTransactionManager"
)
public class CommonDataSourceConfiguration {

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.hikari.hq.common")
    public DataSourceProperties commonDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name="commonDataSource")
    @ConfigurationProperties("spring.datasource.hikari.hq.common.configuration")
    public DataSource commonDataSource() {
        return new LazyConnectionDataSourceProxy(commonDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build());
    }

    @Primary
    @Bean(name = "commonEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean commonEntityManagerFactory(EntityManagerFactoryBuilder builder) {

        return builder
                .dataSource(commonDataSource())
                .packages("com.autofocus.pms.hq.domain.common", "com.autofocus.pms.hq.config.securityimpl", "com.autofocus.pms.security.oauth2")
                .persistenceUnit("commonEntityManager")
                .build();
    }

    @Primary
    @Bean(name = "commonTransactionManager")
    public PlatformTransactionManager commonTransactionManager(@Qualifier("commonEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Primary
    @Bean(name="commonSqlSessionFactory")
    public SqlSessionFactory commonSqlSessionFactory(@Qualifier("commonDataSource") DataSource commonDataSource) throws Exception{
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(commonDataSource);

        sessionFactory.setTypeAliasesPackage("com.autofocus.pms.hq.mapper");

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
        sessionFactory.setTypeHandlers(new YNCode.TypeHandler(), new ManagementDepartment.TypeHandler(), new ViewPermission.TypeHandler(), new CustomerGrade.TypeHandler(), new JsonTypeHandler());


        return sessionFactory.getObject();
    }


    @Primary
    @Bean(name="commonSqlSessionTemplate")
    public SqlSessionTemplate commonSqlSessionTemplate(SqlSessionFactory commonSqlSessionFactory) {
        return new SqlSessionTemplate(commonSqlSessionFactory);
    }


    @Primary
    @Bean(name="commonJdbcTemplate")
    public JdbcTemplate commonJdbcTemplate(@Qualifier("commonDataSource") DataSource commonDataSource) {
        return new JdbcTemplate(commonDataSource);
    }

}
