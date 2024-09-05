package com.no1.book.controller.order;

import com.no1.book.domain.order.OrderBotRequest;
import com.no1.book.domain.order.OrderBotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/order")
public class OrderBotController {
    @Value("${order.chat.model}")
    private String model;

    @Value("${order.chat.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/bot")
    public String chat(@RequestParam(name = "prompt") String prompt) {
        OrderBotRequest request = new OrderBotRequest(model, prompt);
        OrderBotResponse response = restTemplate.postForObject(apiUrl, request, OrderBotResponse.class);
        return response.getChoices().get(0).getMessage().getContent();
    }
}
