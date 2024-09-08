package com.autofocus.pms.hq;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

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
