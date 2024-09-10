#!/usr/bin/env python3
#-*- codig: utf-8 -*-
import sys
import requests
import json
from database import get_review
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
content = "제일 마음에 드는 옷을 입고 노란꽃 한 송이를 손에 들고"
data = {
    "content": content
}
print(json.dumps(data))
response = requests.post(url, data=json.dumps(data), headers=headers)
rescode = response.status_code
if(rescode == 200):
    print (response.text)
else:
    print("Error : " + response.text)