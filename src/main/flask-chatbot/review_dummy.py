import pymysql
from faker import Faker
import random
import dotenv
import os

# MySQL 데이터베이스 연결 설정 (pymysql 사용)
db = pymysql.connect(
    host="localhost",    # MySQL 서버 주소
    port=3306,           # MySQL 기본 포트 번호
    user="root",         # MySQL 사용자 이름
    password=os.getenv('DB_PASSWORD'), # MySQL 비밀번호
    database="no1",      # 사용할 데이터베이스 이름
    charset='utf8mb4'    # UTF-8 인코딩 설정
)

cursor = db.cursor()

# Faker 초기화 (한국어 로케일)
fake = Faker('ko_KR')

# 제품(prod_id) 범위 설정
prod_ids = [f'prod{i}' for i in range(1, 465)]

# 고객(cust_id) 설정
cust_ids = [f'cust{j}' for j in range(1, 1000)]  # cust1~cust999까지 생성

# 더미 리뷰 데이터 생성 함수 (리뷰 내용은 한국어 catch_phrase로 생성)
def generate_review(prod_id, cust_id):
    content = fake.catch_phrase()  # 한국어로 짧은 문구 생성
    rcmd_count = random.randint(0, 50)  # 추천 수는 0~50 사이
    cre_date = fake.date_this_year().strftime('%Y-%m-%d %H:%M:%S')  # 올해 생성 날짜
    reg_date = cre_date  # 등록 날짜는 생성 날짜와 동일
    reg_id = cust_id  # 등록자는 리뷰 작성자
    up_date = reg_date  # 업데이트 날짜도 초기에는 등록 날짜와 동일
    up_id = cust_id  # 업데이트한 사용자도 리뷰 작성자로 설정
    star_pt = random.randint(1, 5)  # 평점은 1~5 사이
    sentiment = 'pending'  # 감정 분석 상태는 일단 'pending'

    # SQL 쿼리 생성
    sql = """
    INSERT INTO review (prod_id, cust_id, content, rcmd_count, cre_date, reg_date, reg_id, up_date, up_id, star_pt, sentiment)
    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    """
    values = (prod_id, cust_id, content, rcmd_count, cre_date, reg_date, reg_id, up_date, up_id, star_pt, sentiment)
    return sql, values

# 리뷰 테이블의 기존 데이터를 모두 삭제하는 쿼리
cursor.execute("DELETE FROM review")
db.commit()

# 각 제품에 대해 2~6개의 리뷰 생성
for prod_id in prod_ids:
    num_reviews = random.randint(2, 6)  # 각 제품마다 2~6개의 리뷰 생성
    for _ in range(num_reviews):
        cust_id = random.choice(cust_ids)  # 고객 ID를 랜덤으로 선택
        sql, values = generate_review(prod_id, cust_id)
        cursor.execute(sql, values)

# 변경사항 저장
db.commit()

# 커서 및 연결 종료
cursor.close()
db.close()

print("더미 데이터 생성 완료!")