package com.no1.book.common.config.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ProductAppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}