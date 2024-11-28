package io.github.patternhelloworld.tak.config.resttemplate;

import io.github.patternhelloworld.tak.config.logger.module.RestTemplateClientErrorLogConfig;
import io.github.patternhelloworld.tak.config.logger.module.RestTemplateClientLogConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final RestTemplateBuilder restTemplateBuilder;

    private RestTemplate createRestTemplate(String rootUri) {
        return restTemplateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .rootUri(rootUri)
                .additionalInterceptors(new RestTemplateClientLogConfig())
                .errorHandler(new RestTemplateClientErrorLogConfig())
                .setConnectTimeout(Duration.ofSeconds(6))
                .build();
    }

}
