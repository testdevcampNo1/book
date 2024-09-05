package com.no1.book.common.config.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OrderAppConfig {
    @Value("${order.chat.api.key}")
    private String openAiKey;

    // restful
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
           request.getHeaders().add("Authorization", "Bearer " + openAiKey);
           return execution.execute(request, body);
        });
        return restTemplate;
    }
}
