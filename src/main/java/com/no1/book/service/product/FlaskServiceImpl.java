package com.no1.book.service.product;


import com.no1.book.dao.product.ProductDao;
import com.no1.book.domain.product.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Book;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlaskServiceImpl implements FlaskService {

    private final RestTemplate restTemplate;

    @Autowired
    private ProductDao productDao;

    public FlaskServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String sendDataToFlask(Map<String, Object> payload, String endPoint) {

        String flaskUrl = "http://127.0.0.1:5000/" + endPoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

//    @Override
//    public Map<String, Object> preprocessData(Map<String, Object> payload) {
//        payload.put("processed", true);
//        return payload;
//    }

    @Override
    public String sendTopSellingBooksToFlask() throws Exception {
        // DB에서 판매량이 가장 높은 책 5개를 가져옴
        List<ProductDto> topSellingBooks = productDao.orderBySales().subList(0,5);;

        // Flask 서버로 데이터 전송
        String flaskUrl = "http://127.0.0.1:5000/receive-data";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<ProductDto>> entity = new HttpEntity<>(topSellingBooks, headers);
        ResponseEntity<String> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    @Override
    public String sendTopRatedBooksToFlask() throws Exception {
        // DB에서 별점이 가장 높은 책 5개를 가져옴
        List<ProductDto> topRatedBooks = productDao.orderByStar().subList(0,5);

        // Flask 서버로 데이터 전송
        String flaskUrl = "http://127.0.0.1:5000/receive-data";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<ProductDto>> entity = new HttpEntity<>(topRatedBooks, headers);
        ResponseEntity<String> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

}