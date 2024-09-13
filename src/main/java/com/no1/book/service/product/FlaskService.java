package com.no1.book.service.product;

import java.util.Map;

public interface FlaskService {

    String sendDataToFlask(Map<String, Object> payload, String endPoint);

    // 데이터 전처리 함수
//    Map<String, Object> preprocessData(Map<String, Object> payload);

    String sendTopSellingBooksToFlask() throws Exception;

    String sendTopRatedBooksToFlask() throws Exception;
}
