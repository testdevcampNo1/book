package com.no1.book.controller.product;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FromFlaskController {

    @PostMapping("/receive-data")
    public String receiveDataFromFlask(@RequestBody Map<String, Object> data) {
        System.out.println("Received data from Flask: " + data);
        return "Data received successfully";
    }
}