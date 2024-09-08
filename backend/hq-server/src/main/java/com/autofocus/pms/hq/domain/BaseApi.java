package com.autofocus.pms.hq.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseApi {

    @Value("${spring.profiles.active}")
    private String activeProfile;

/*    @Autowired
    private ServletWebServerApplicationContext server;*/

    @GetMapping("/systemProfile")
    public String getProfile () {
        return activeProfile;
    }


    @GetMapping("/")
    public String home () {
        return "home";
    }

}
