package com.no1.book.domain.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBotRequest {
    private String model;
    private List<OrderBotMessage> messages;

    public OrderBotRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new OrderBotMessage("user", prompt));
    }
}
