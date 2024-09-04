package com.no1.book.controller.product;

import com.no1.book.service.product.FlaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ToFlaskController {

    @Autowired
    private final FlaskService flaskService;

    public ToFlaskController(FlaskService flaskService) {
        this.flaskService = flaskService;
    }

    @PostMapping("/send-data")
    public String sendDataToFlask(@RequestBody Map<String, Object> payload) {
        // Flask 서버로 데이터 전송
        return flaskService.sendDataToFlask(payload);
    }

    @PostMapping("/send-top-selling-books")
    public String sendTopSellingBooks() throws Exception {
        // DB에서 판매량이 가장 높은 책 5개를 가져와서 Flask 서버로 전송
        return flaskService.sendTopSellingBooksToFlask();
    }

    @PostMapping("/send-top-rated-books")
    public String sendTopRatedBooks() throws Exception {
        // DB에서 별점이 가장 높은 책 5개를 가져와서 Flask 서버로 전송
        return flaskService.sendTopRatedBooksToFlask();
    }
}