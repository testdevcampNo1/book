package com.no1.book.service.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class APIServiceImpl implements APIService {

    @Value("${naver.api.clientId}")
    private String clientId;

    @Value("${naver.api.clientSecret}")
    private String clientSecret;

    public String translateText(String textToTranslate) throws IOException {
        String text = URLEncoder.encode(textToTranslate, "UTF-8"); // 번역할 텍스트를 UTF-8로 인코딩
        String apiURL = "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation"; // 번역 API URL

        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST"); // POST 방식으로 요청
        con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId); // 클라이언트 ID 설정
        con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret); // 클라이언트 시크릿 설정

        String postParams = "source=ko&target=en&text=" + text; // 요청 파라미터 설정 (한국어에서 영어로 번역)

        con.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(postParams); // 요청 파라미터 전송
            wr.flush();
        }

        int responseCode = con.getResponseCode();
        BufferedReader br;
        if (responseCode == 200) { // 응답이 성공적일 때
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else { // 오류 발생 시
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine); // 응답 결과를 StringBuilder에 추가
        }
        br.close();
        return response.toString(); // 번역 결과 반환
    }
}