# PMS Common

## Overview
- 모든 PMS 프로젝트들의 공통 모듈로써 Payload 와 오류 처리를 담당

## Features
### Payload
#### 성공
- 모든 성공 response (2xx, 현재는 200 만 존재) 는 항상 예외 없이 다음 Payload 를 따른다.
````json
{
  "timestamp" : "2023-11-01T01:50:40.037+0000",
  "details" : "/api/v1/binders",

  "data" : "<--각 API 들에 따라 다양한 Type 이 올 수 있음-->",
}
````
#### 실패
- 모든 실패 response (200 제외) 는 항상 다음 Payload 를 따른다.
````json
# ※ 처리 되지 않은 (Unhandled) 오류 (항상 Status Code 는 500)
{
    "timestamp": "2023-10-23T03:59:59.357+0000",
    "details": "uri=/api/v1/clinics",

    "message": "",   # 이 메시지는 StackTrace를 포함하고 있어 보안상 production에서 클라이언트에 빈 값으로 전달됩니다.
    "userMessage": "처리되지 않은 오류 입니다.",
    "userValidationMessage": null,
}

# ※ 처리 된 (Handled) 오류 (500(처리 되지 않은 오류)을 제외 한 모든 오류 코드.)
{
    "timestamp": "2023-10-23T03:41:53.424+0000",
    "details": "uri=/api/v1/customer/create",

    "message": "could not execute statement; SQL [n/a]; constraint [customer.email]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
            # 이 message 필드는 StackTrace를 포함할 수도 있어, 보안상 production에서 클라이언트에 빈 값으로 전달됩니다.
    "userMessage": null,
    "userValidationMessage": {
        "email": "이미 등록된 이메일이 있습니다."
    }
}
````
- "userMessage" 프로퍼티는 호출한 API 에 해당하는 오류, "userValidationMessage" 호출한 API 의 클라이언트가 전달한 개별 파라매터에 해당하는 오류이다.
- 클라이언트의 처리 방법 : 결론적으로 Http Status 관계없이, userMessage 파라매터의 값이 있으면, 이를 안내 창으로 띄우고, 없을 경우 userValidationMessage 의 key, value 값에서 value 값을 해당 key value 에 해당하는 창에 표시하거나, 안내 창으로 띄운다.

## Installation
- 사용하고자 하는 모듈의 진입점에서 이 모듈의 Bean 들을 읽을 수 있게 다음과 같이 등록해준다. ("com.autofocus.pms.common")
- 예시는 com.autofocus.pms.hq 모듈에서 이 모듈을 가져다 쓰는 것으로 하였다.

```java
@EnableScheduling
@SpringBootApplication(scanBasePackages =  {"com.autofocus.pms.common", "com.autofocus.pms.hq"})
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
- hq-server 모듈의 ``application.properties``에 필요한 설정 파일들을 추가한다.
```properties
spring.jpa.properties.hibernate.dialect=com.autofocus.pms.hq.config.database.dialect.CustomMySQL8Dialect
```