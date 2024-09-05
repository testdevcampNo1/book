package com.no1.book.controller.product;

import com.no1.book.service.product.FlaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ToFlaskController {

    @Autowired
    private final FlaskService flaskService;

    public ToFlaskController(FlaskService flaskService) {
        this.flaskService = flaskService;
    }

    @PostMapping("/send-data")
    public String sendDataToFlask(@RequestBody Map<String, Object> payload, String endPoint) {
        // Flask 서버로 데이터 전송
        return flaskService.sendDataToFlask(payload, endPoint);
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


//    // 스프링 컨트롤러에서 세션의 custId를 가져오기
//    @GetMapping("/get-session-id")
//    public ResponseEntity<Map<String, String>> getSessionId(HttpSession session) {
//        String custId = (String) session.getAttribute("custId");
//        if (custId == null) {
//            System.out.println("포인트");
//            custId = "";
//        }
//        Map<String, String> response = new HashMap<>();
//        response.put("custId", custId);
//        System.out.println("response : " + response);
//        return ResponseEntity.ok(response);
//    }
}