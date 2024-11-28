package io.github.patternhelloworld.tak;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication(scanBasePackages =  {"io.github.patternhelloworld.common", "io.github.patternhelloworld.tak", "io.github.patternknife.securityhelper.oauth2.api"})
public class TakApplication {

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(TakApplication.class, args);
    }

}
