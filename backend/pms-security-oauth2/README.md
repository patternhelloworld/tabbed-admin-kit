# PMS-Security-OAuth2

## Overview
- 표준 Oauth2 의 'Resource Owner Password Credentials Grant (https://velog.io/@wooyoung-tom/oauth 의 3 번째) 방식을 구현한 Security 공통 모듈


## Requirements
- Spring Security 6 + spring-security-oauth2-authorization-server 를 필요로 함
````xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-oauth2-authorization-server</artifactId>
        </dependency>
````
- 모든 PMS 프로젝트 공통 모듈인 pms-common ( https://vholic.com:9094/autofocus/pms-common ) 를 필요로 함
````xml
    <dependency>
            <groupId>com.autofocus.pms.common</groupId>
            <artifactId>pms-common</artifactId>
            <version>FINAL</version>
    </dependency>

````

## Features
- '/oauth2/token'과 컨트롤러 레이어에 있는 '/api/v1...' 두 경로에 대해 액세스 및 리프레시 토큰 API를 설정했습니다. 이 두 경로는 동일한 기능을 가지며, 성공 및 오류에 대한 요청 및 응답 페이로드도 동일합니다.
- 오류 페이로드는 pms-common 프로젝트를 따릅니다. ('message'는 클라이언트에게 노출되지 않으나 로그 파일에는 남으며, 대신 'userMessage'가 노출됩니다.)
````json
{
  "timestamp": 1719470948370,
  "message": "Couldn't find the client ID : client_admi",
  "details": "uri=/oauth2/token",
  "userMessage": "Authentication failed. Please check your credentials.",
  "userValidationMessage": null
}
````
- 사용자 이름, 클라이언트 ID 및 추가 토큰(App-Token 이라고 하며, 호출 장치로부터 고유 값을 받음)을 조합하여 인증 관리를 수행합니다.
- DB는 ``oauth_access_token, oauth_refresh_token, oauth_client_details`` 로 구성

## Installation
- 사용하고자 하는 모듈의 진입점에서 이 모듈의 Bean 들을 읽을 수 있게 다음과 같이 등록해준다. ("com.autofocus.pms.security.oauth2")
- 예시는 com.autofocus.pms.hq 모듈에서 이 모듈을 가져다 쓰는 것으로 하였다.

```java
@EnableScheduling
@SpringBootApplication(scanBasePackages =  {"com.autofocus.pms.common", "com.autofocus.pms.hq", "com.autofocus.pms.security.oauth2"})
public class HqApplication {

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(HqApplication.class, args);
    }

}
```
- 해당 모듈 ("com.autofocus.pms.security.oauth2") 은 JPA 를 사용하므로 다음 두 곳에 추가 등록해준다.
```java
@Configuration
@MapperScan(basePackages = {"com.autofocus.pms.hq.mapper"}, sqlSessionFactoryRef = "commonSqlSessionFactory")
@EnableJpaRepositories(
        basePackages = {"com.autofocus.pms.hq.domain", "com.autofocus.pms.hq.config.securityimpl", "com.autofocus.pms.security.oauth2"},
        entityManagerFactoryRef = "commonEntityManagerFactory",
        transactionManagerRef= "commonTransactionManager"
)
public class CommonDataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari.merz")
    public DataSourceProperties commonDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name="commonDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.hikari.merz.configuration")
    public DataSource commonDataSource() {
        return new LazyConnectionDataSourceProxy(commonDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build());
    }

    @Primary
    @Bean(name = "commonEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean commonEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(commonDataSource())
                .packages("com.autofocus.pms.hq.domain", "com.autofocus.pms.hq.config.securityimpl", "com.autofocus.pms.security.oauth2")
                .persistenceUnit("commonEntityManager")
                .build();
    }

    @Primary
    @Bean(name = "commonTransactionManager")
    public PlatformTransactionManager commonTransactionManager(@Qualifier("commonEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    @Bean(name="commonSqlSessionFactory")
    public SqlSessionFactory commonSqlSessionFactory(@Qualifier("commonDataSource") DataSource commonDataSource) throws Exception{
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(commonDataSource);

        sessionFactory.setTypeAliasesPackage("com.autofocus.pms.hq.mapper");

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));


        return sessionFactory.getObject();
    }

    @Bean(name="commonSqlSessionTemplate")
    public SqlSessionTemplate commonSqlSessionTemplate(SqlSessionFactory commonSqlSessionFactory) {
        return new SqlSessionTemplate(commonSqlSessionFactory);
    }

    @Bean(name="commonJdbcTemplate")
    public JdbcTemplate commonJdbcTemplate(@Qualifier("commonDataSource") DataSource commonDataSource) {
        return new JdbcTemplate(commonDataSource);
    }

}

```
- hq-server 모듈의 ``application.properties``에 필요한 설정 파일들을 추가한다.
```properties
server.error.whitelabel.enabled=false
management.endpoints.web.exposure.include=*
```