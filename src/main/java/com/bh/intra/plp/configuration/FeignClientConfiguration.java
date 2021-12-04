package com.bh.intra.plp.configuration;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {
    @Value("${config.oauth2.clientId}")
    private String clientId;

    @Value("${config.oauth2.clientSecret}")
    private String clientSecret;
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(clientId, clientSecret);
    }
}
