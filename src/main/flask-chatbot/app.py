from flask import Flask, request, jsonify, render_template, session
from flask_cors import CORS
from openai import OpenAI
from dotenv import load_dotenv
import requests
import os
from database import get_product, get_cart
from api_specifi import get_api_routes

load_dotenv()

app = Flask(__name__)
app.secret_key = os.urandom(24)  # FLask에서 세션 사용하려면 비밀키 지정해야한다고 함

CORS(app)

api_key = os.getenv('OPENAI_API_KEY')
client = OpenAI(api_key=api_key)

messages = []

@app.route('/')
def home():
    return "Hello, Flask!"

def make_prompt(user_input):
    res = client.chat.completions.create(
        model='gpt-4o-mini',
        messages=user_input
    )
    return res.choices[0].message.content


@app.route('/chatbot', methods=["GET", "POST"])
def chatbot():
    if request.method == "POST":

        print("session get : ", session.get('cust_id')) 
        # POST 요청이 들어오면 사용자의 입력을 받아 처리
        user_input = request.json.get("message")
        
        # 대화 기록을 기반으로 OpenAI에게 응답을 요청
        conversation = [{"role":"system", "content":few_shot()}] 

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
    print("session get : ", session.get('cust_id')) 
    content = (
        "너는 도서 쇼핑몰 챗봇이야. "
        "내가 너에게 제공하는 DB 내용을 기반으로 사용자 질문에 대한 적절한 답변을 제시해야 해. "
        "사용자의 요청에 대한 판단은 내가 너에게 제공하는 DB에 기반해서 네가 직접 해야 해. "
        "이건 상품 데이터에 대한 DB 정보야: {}. "
        "이건 사이트 api 명세야: {}. "
        "사이트 이용에 대한 문의가 들어오면 api 명세를 활용해서 답변을 해주면 돼. "
        "이건 현재 사용자의 id야: {}. "
        "이건 장바구니 정보야: {}. "
        "만약 회원이 장바구니에 담긴 상품을 묻는다면 사용자 id와 장바구니 table의 cust_id가 일치하는 품목들에 대해 답변을 해줘야 해. "
        "이건 사용자가 현재 존재하는 페이지의 상품 id야: {}"
        "해당 상품에 대해 물어본다면 상품 id에 맞는 도서를 찾아서 너가 아는대로 대답하면 돼."
        "또한 지금부터 예측되는 사용자 질문에 대한 답변 가이드를 너에게 제시할 거야. "
        "잘 참고해서 실제 답변에 활용하길 바랄게. "
        "책 추천의 경우 되도록이면 DB내 존재하는 책들로 추천해줘. "
        "가이드 답변 내에서 내가 너에게 하는 말은 -- 안에다가 작성할게. "
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
        "\n\n product 테이블의 ord_chk_code는 AVBL일 경우 판매가능 OSKT는 일시품절 STOP은 판매중지야. "
        "레코드의 개별적인 정보에 대해서는 제공할 필요 없어 이를테면 ' - 새벽배송 여부: **Y** - 판매 상태: **AVBL** ' 이런거 답변에 포함하지 말라는거야. "
        "너가 답변을 마크다운 형식으로 하는 경향이 있는 것 같아. 답변을 마크다운 형식으로 주지 말되 줄바꿈은 신경써서 해줘."
    ).format(
        get_product(), 
        get_api_routes(), 
        cust_id, 
        get_cart(cust_id),
        prod_id
    )

    return content


# 스프링에서 보내는 cust_id 받기
@app.route('/receive-cust-id', methods=['POST'])
def receive_cust_id_from_spring():
    # cust_id = request.json.get('custId')
    # session['cust_id'] = cust_id  # 세션에 custId 저장
    # print(f"custId from spring: {cust_id}")
    global cust_id
    cust_id = request.json.get('custId')
    print("global cust id : ", cust_id)
    return jsonify({"status": "success", "received_data": cust_id})


# 스프링에서 보내는 prod_id 받기
@app.route('/receive-prod-id', methods=['POST'])
def receive_prod_id_from_spring():
    global prod_id
    prod_id = request.json.get('prodId')
    print(f"prodId from spring: {prod_id}")
    print("globa prod id : ", prod_id)
    return jsonify({"status": "success", "received_data": prod_id})







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