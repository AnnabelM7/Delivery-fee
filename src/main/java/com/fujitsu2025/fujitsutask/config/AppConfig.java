package com.fujitsu2025.fujitsutask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// Configuration class for Spring Bean

@Configuration
public class AppConfig {
    // Creates and provides RestTemplate bean
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); //HTTP (GET; POST ECT)
    }
}