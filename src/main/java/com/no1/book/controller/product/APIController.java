package com.no1.book.controller.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.no1.book.service.product.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIController {

    @Autowired
    private APIService apiService;

    @PostMapping("/detail/translate")
    public ResponseEntity<String> translate(@RequestBody String text) throws Exception {
        // 번역 서비스 호출
        String translatedTextJson = apiService.translateText(text);

        // JSON 응답에서 "translatedText" 추출
        ObjectMapper objectMapper = new ObjectMapper(); // 자바 객체를 JSON 문자열로 변환
        JsonNode rootNode = objectMapper.readTree(translatedTextJson);
        String translatedText = rootNode.path("message").path("result").path("translatedText").asText();
        return ResponseEntity.ok(translatedText);  // 번역된 텍스트만 반환
    }
}
