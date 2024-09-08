package com.autofocus.pms.hq.config.database;

import com.autofocus.pms.hq.config.database.typeconverter.*;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;


@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.autofocus.pms.hq.mapper"}, sqlSessionFactoryRef = "crmSqlSessionFactory")
@EnableJpaRepositories(
        basePackages = {"com.autofocus.pms.hq.domain.crm"},
        entityManagerFactoryRef = "crmEntityManager",
        transactionManagerRef= "crmTransactionManager"
)
public class CrmDataSourceConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.hikari.hq.crm")
    public DataSourceProperties crmDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name="crmDataSource")
    @ConfigurationProperties("spring.datasource.hikari.hq.crm.configuration")
    public DataSource crmDataSource() {
        return new LazyConnectionDataSourceProxy(crmDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build());
    }
    @Autowired
    private Environment env;

    @Bean(name = "crmEntityManager")
    public LocalContainerEntityManagerFactoryBean crmEntityManager() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(crmDataSource());
        em.setPackagesToScan("com.autofocus.pms.hq.domain.crm", "com.autofocus.pms.hq.config.securityimpl", "com.autofocus.pms.security.oauth2");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect",env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;

    }

    @Bean(name = "crmTransactionManager")
    public PlatformTransactionManager crmTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(crmEntityManager().getObject());
        return transactionManager;
    }

    @Bean(name="crmSqlSessionFactory")
    public SqlSessionFactory crmSqlSessionFactory(@Qualifier("crmDataSource") DataSource crmDataSource) throws Exception{
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(crmDataSource);

        sessionFactory.setTypeAliasesPackage("com.autofocus.pms.hq.mapper");

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));

        sessionFactory.setTypeHandlers(new YNCode.TypeHandler(), new ViewPermission.TypeHandler(), new PurchasePlan.TypeHandler(), new Nationality.TypeHandler(), new ManagementDepartment.TypeHandler(), new Gender.TypeHandler(), new CustomerGrade.TypeHandler());

        return sessionFactory.getObject();
    }


    @Bean(name="crmSqlSessionTemplate")
    public SqlSessionTemplate crmSqlSessionTemplate(SqlSessionFactory crmSqlSessionFactory) {
        return new SqlSessionTemplate(crmSqlSessionFactory);
    }


    @Bean(name="crmJdbcTemplate")
    public JdbcTemplate crmJdbcTemplate(@Qualifier("crmDataSource") DataSource crmDataSource) {
        return new JdbcTemplate(crmDataSource);
    }

}
