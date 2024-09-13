#!/usr/bin/env python3
#-*- coding: utf-8 -*-
import sys
import requests
import json
from database import get_review, get_pending_review, update_pending_review
from dotenv import load_dotenv
import os

load_dotenv()

client_id = os.getenv('NAVER_CLOUD_KEY_ID')
client_secret = os.getenv('NAVER_CLOUD_KEY')
url="https://naveropenapi.apigw.ntruss.com/sentiment-analysis/v1/analyze"
headers = {
    "X-NCP-APIGW-API-KEY-ID": client_id,
    "X-NCP-APIGW-API-KEY": client_secret,
    "Content-Type": "application/json"
}

def eval_sentiment(content):
    data = {
        "content": content
    }
    
    response = requests.post(url, data=json.dumps(data), headers=headers)
    rescode = response.status_code
    
    if rescode == 200:
        # 응답을 JSON 형식으로 변환
        response_json = response.json()
        
        # 긍정, 부정, 중립 확률 추출
        positive_confidence = response_json['document']['confidence']['positive']
        negative_confidence = response_json['document']['confidence']['negative']
        
        print(f"긍정 확률: {positive_confidence}%")
        print(f"부정 확률: {negative_confidence}%")
        
        # 긍정과 부정 확률을 비교
        if positive_confidence > negative_confidence:
            sentiment = 'positive'
        else:
            sentiment = 'negative'
        
        print(f"<< 감정 분석 결과 : {sentiment} >>")
        
        return sentiment
    else:
        print("Error : " + response.text)
        return None

# 감정 분석 대기 중인 리뷰를 조회하고 분석을 수행
def review_update():
    pending_reviews = get_pending_review()  # 감정 분석 대기 중인 리뷰 가져오기
    total_reviews = len(pending_reviews)  # 전체 대기 중인 리뷰 개수
    processed_reviews = 0  # 분석 완료된 리뷰 개수
    
    if total_reviews == 0:
        print("감정 분석 대기 중인 리뷰가 없습니다.")
        return

    i = 1
    
    for review in pending_reviews:
        content = review['content']
        review_id = review['review_id']
        
        sentiment = eval_sentiment(content)  
        if sentiment:
            update_pending_review(review_id, sentiment)
            processed_reviews += 1  # 분석 완료된 리뷰 수 증가
    
        # 분석 완료된 리뷰와 전체 대기 중인 리뷰 수 출력
        print(f"- {i}/{total_reviews} 리뷰 분석 완료 -\n")
        i += 1
