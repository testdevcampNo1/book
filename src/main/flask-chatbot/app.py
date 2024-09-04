from flask import Flask, request, jsonify, render_template
from flask_cors import CORS
from openai import OpenAI
from dotenv import load_dotenv
import requests
import os
from database import get_product

load_dotenv()

app = Flask(__name__)
CORS(app)

api_key = os.getenv('OPENAI_API_KEY')
client = OpenAI(api_key=api_key)

messages = []

@app.route('/')
def home():
    return "Hello, Flask!"

def make_prompt(user_input):
    res = client.chat.completions.create(
        model='gpt-4o',
        messages=user_input
    )
    return res.choices[0].message.content


@app.route('/chatbot', methods=["GET", "POST"])
def chatbot():
    if request.method == "POST":
        # POST 요청이 들어오면 사용자의 입력을 받아 처리
        user_input = request.json.get("message")
        
        # 대화 기록을 기반으로 OpenAI에게 응답을 요청
        conversation = [{"role":"system", "content":few_shot()}] 
        # content에서 질문에 따른 답변을 few shot 러닝 시킬수 있는듯... 그럼 db 조회는 어떻게?
        # 따로 db 조회 함수를 만들어서 content 안에 삽입 하면 될듯? 아마도?
        # 스프링에서 보내는 정보들은? (세션에 담긴 custId나 해당 제품 상세 페이지의 prodId 등) .....
        # 그건 일단 나중에 생각
        

        conversation.extend(messages)
        conversation.append({"role": "user", "content": user_input})
        
        # OpenAI API를 호출하여 챗봇 응답 생성
        bot_response = make_prompt(conversation)
        
        # 대화 기록에 추가
        messages.append({"role": "user", "content": user_input})
        messages.append({"role": "assistant", "content": bot_response})

        return jsonify({"message": bot_response})

    else:
        # GET 요청이 들어오면, chatbot.html 페이지를 반환
        return render_template("chatbot.html")



def few_shot():
    content = (  # 파이썬에서 소괄호 안의 문자열은 하나의 문자열로 처리됨
        "너는 도서 쇼핑몰 챗봇이야. "
        "내가 너에게 제공하는 DB 내용을 기반으로 사용자 질문에 대한 적절한 답변을 제시해야 해. "
        "사용자의 요청에 대한 판단은 내가 너에게 제공하는 DB에 기반해서 네가 직접 해야 해. "
        "이건 상품 데이터에 대한 DB 정보야: {}. "
        "또한 지금부터 예측되는 사용자 질문에 대한 답변 가이드를 너에게 제시할 거야. "
        "잘 참고해서 실제 답변에 활용하길 바랄게. "
        "책 추천의 경우 되도록이면 DB내 존재하는 책들로 추천해주고 너가 판단하기에 DB내에 추천해줄 책이 없다면 다른 유명한 책으로 추천하고 구글에 검색 링크만 제공하도록 해."
        "가이드 답변 내에서 내가 너에게 하는 말은 -- 안에다가 작성할게"
        "\n\nQ: 요즘 심리학에 관심을 갖게 되었어. 심리학에 대한 책을 추천해줄래?\n"
        "A: 심리학에 빠진 당신을 위해 이 책을 추천합니다 -DB 조회 결과 바탕으로 책 추천-\n"
        "사이트 내 책 검색: 'http://localhost:8080/product/list?cateKey=&keyword=-여기에 책 제목-'\n"
        "구글에 책 검색: 'https://www.google.com/search?q=-여기에 책 제목-&sourceid=chrome&ie=UTF-8'\n"
        "\n\nQ: 1만원에서 2만원 사이로 재미있는 만화책 하나 추천해줄래??\n"
        "A: 이 책은 어떠신가요? -DB 조회 결과 바탕으로, 사용자 요구에 맞게(가격) 책 추천-\n"
        "사이트 내 책 검색: 'http://localhost:8080/product/list?cateKey=&keyword=-여기에 책 제목-'\n"
        "구글에 책 검색: 'https://www.google.com/search?q=-여기에 책 제목-&sourceid=chrome&ie=UTF-8'\n"
        "\n\nQ: 역사와 관련된 책 세 권만 추천해줄래?\n"
        "A: 역사와 관련된 다음 책들을 추천합니다. -DB 조회 결과 바탕으로, 사용자 요구에 맞게(개수) 책 추천-\n"
        "사이트 내 -책 제목1- 검색: 'http://localhost:8080/product/list?cateKey=&keyword=-여기에 책 제목-'\n"
        "구글에 -책 제목1- 검색: 'https://www.google.com/search?q=-여기에 책 제목-&sourceid=chrome&ie=UTF-8'\n"
        "사이트 내 -책 제목2- 검색: 'http://localhost:8080/product/list?cateKey=&keyword=-여기에 책 제목-'\n"
        "구글에 -책 제목2- 검색: 'https://www.google.com/search?q=-여기에 책 제목-&sourceid=chrome&ie=UTF-8'\n"
        "사이트 내 -책 제목3- 검색: 'http://localhost:8080/product/list?cateKey=&keyword=-여기에 책 제목-'\n"
        "구글에 -책 제목3- 검색: 'https://www.google.com/search?q=-여기에 책 제목-&sourceid=chrome&ie=UTF-8'\n"
        "\n\n product 테이블의 ord_chk_code는 AVBL일 경우 판매가능 OSKT는 일시품절 STOP은 판매중지야\n"
        "레코드의 개별적인 정보에 대해서는 제공할 필요 없어 이를테면 ' - 새벽배송 여부: **Y** - 판매 상태: **AVBL** ' 이런거 답변에 포함하지 말라는거야."

    ).format(get_product())  # DB에서 상품 정보를 가져오는 함수 호출 (이후에 다른 함수도 추가할 예정)

    return content 




# 스프링에서 보내는 데이터 받는 테스트 메서드
@app.route('/receive-data', methods=['POST'])
def receive_data():
    data = request.json
    print(f"Received data: {data}")
    return jsonify({"status": "success", "received_data": data})

# 스프링으로 데이터 보내는 테스트 메서드
@app.route('/send-to-spring', methods=['GET'])
def send_data_to_spring():
    spring_url = "http://localhost:8080/receive-data"
    data = {
        "title": "Example Book",
        "author": "John Doe",
        "rating": 4.5
    }

    response = requests.post(spring_url, json=data)

    if response.status_code == 200:
        return f"Data sent successfully to Spring Boot: {response.text}"
    else:
        return f"Failed to send data. Status code: {response.status_code}"


if __name__ == '__main__':
    app.run(debug=True)