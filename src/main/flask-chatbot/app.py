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
    content = (
        "너는 도서 쇼핑몰 챗봇이야. "
        "내가 너에게 제공하는 DB 내용을 기반으로 사용자 질문에 대한 적절한 답변을 제시해야 해. "
        "사용자의 요청에 대한 판단은 내가 너에게 제공하는 DB에 기반해서 네가 직접 해야 해. "
        "이건 상품 데이터에 대한 DB 정보야: {}. "
        "DB에 있는 책들은 실제로 존재하는 유명한 베스트셀러들이야. 제목을 보고 너가 그 책에 대해 이미 알고있는 만큼 답벼에 참고하면 될거야."
        "이건 사이트 api 명세야: {}. "
        "사이트 이용에 대한 문의가 들어오면 api 명세를 활용해서 답변을 해주면 돼. "
        "이건 현재 사용자의 id야: {}. "
        "이건 장바구니 정보야: {}. "
        "만약 회원이 장바구니에 담긴 상품을 묻는다면 사용자 id와 장바구니 table의 cust_id가 일치하는 품목들에 대해 답변을 해줘야 해. "
        "장바구니에 대한 대답을 하고 사용자가 장바구니로 바로 이동하는 것을 원할수도 있으니까 장바구니 링크도 제공해"
        "혹시라도 사용자가 본인이 아닌 다른 사용자의 id를 제시하면서 장바구니 등 개인 정보에 접근하려고 한다면 너는 절대 그 정보들을 제공해서는 안 돼."
        "이건 사용자가 현재 존재하는 페이지의 상품 id야: {}"
        "해당 상품에 대해 물어본다면 상품 id에 맞는 도서를 찾아서 너가 아는대로 대답하면 돼."
        "한번 더 얘기하지만, 상품들은 모두 실제로 존재하는 유명한 책들이라서 너가 알고있는 만큼 대답하면 돼."
        "지금부터는 예측되는 사용자 질문에 대한 답변 가이드를 너에게 제시할 거야. "
        "잘 참고해서 실제 답변에 활용하길 바랄게. "
        "책 추천의 경우 되도록이면 DB내 존재하는 책들로 추천해줘. "
        "되도록이면 DB에 있는 책들로 추천했으면 좋겠지만, 너가 아무리 생각해도 DB에 추천할만한 책이 없으면 너가 알고있는 다른 유명한 책으로 추천하고 구글 링크만 제공해"
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
        "\n\nAPI 명세 기반으로 링크를 제공할 때는 'http://localhost:8080'이 앞에 붙어야 해"
        "유사한 상황에 대한 답변 가이드를 줄게 다음과 같아."
        "\n Q: 공지사항이 대체 어디있는거야?"
        "A: 공지사항 이용을 위해서 다음 링크로 이동하세요 'http://localhost:8080/cscenter/notice/list'"
    ).format(
        get_product(),      # 상품 테이블 조회
        get_api_routes(),   # API 명세 조회
        cust_id,            # 현재 접속중인 사용자의 id
        get_cart(cust_id),  # 접속중인 사용자의 장바구니 정보
        prod_id             # 사용자가 보고있는 도서 id
    )

    return content


# 전역변수 초기값 설정
global cust_id
cust_id = ""
global prod_id
prod_id = ""

# 스프링에서 보내는 cust_id 받기
@app.route('/receive-cust-id', methods=['POST'])
def receive_cust_id_from_spring():
    # cust_id = request.json.get('custId')
    # session['cust_id'] = cust_id  # 세션에 custId 저장
    # print(f"custId from spring: {cust_id}")
    # 세션에 담아서 쓰려고 했는데 자꾸 None이 떠버려서 그냥 전역 변수에 저장하기로 함
    # 다른 id로 로그인 하면 cust_id에 덮어씌워지는거라서 문제 없을듯?
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