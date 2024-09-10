import pymysql
from dotenv import load_dotenv
import os

# 데이터베이스 연결 함수
def get_db_connection():
    return pymysql.connect(
        host='127.0.0.1',
        port=3306,
        user='root',
        password=os.getenv('DB_PASSWORD'),
        db='no1',
        charset='utf8'
    )

# 모든 상품 정보 조회
def get_product():
    connection = get_db_connection()
    cursor = connection.cursor(pymysql.cursors.DictCursor) # DictCursor -> 결과를 dict로 저장
    
    try:
        sql = "SELECT prod_name, prod_id, sale_price, total_sales, star_avg FROM product"
        cursor.execute(sql)
        products = cursor.fetchall()  # DB에서 가져온 결과
        
        return products
    finally:
        cursor.close()
        connection.close()


# 장바구니 조회
def get_cart(cust_id):
    connection = get_db_connection()
    cursor = connection.cursor(pymysql.cursors.DictCursor)
    
    try:
        # sql = "SELECT * FROM cart WHERE cust_id = %s"
        sql = "SELECT * FROM cart WHERE cust_id = %s"
        cursor.execute(sql, (cust_id,))
        products = cursor.fetchall() 
        
        return products
    finally:
        cursor.close()
        connection.close()


# FAQ 게시판 조회
def get_FAQ():
    connection = get_db_connection()
    cursor = connection.cursor(pymysql.cursors.DictCursor) 
    
    try:
        sql = "SELECT * FROM boardFAQ"
        cursor.execute(sql)
        products = cursor.fetchall()  
        
        return products
    finally:
        cursor.close()
        connection.close()
        
# 공지사항 게시판 조회
def get_notice():
    connection = get_db_connection()
    cursor = connection.cursor(pymysql.cursors.DictCursor) 
    
    try:
        sql = "SELECT * FROM boardNotice"
        cursor.execute(sql)
        products = cursor.fetchall()  
        
        return products
    finally:
        cursor.close()
        connection.close()

# QNA 게시판 조회
def get_QNA():
    connection = get_db_connection()
    cursor = connection.cursor(pymysql.cursors.DictCursor)
    
    try:
        sql = "SELECT * FROM boardQNA"
        cursor.execute(sql)
        products = cursor.fetchall()  
        
        return products
    finally:
        cursor.close()
        connection.close()
        
# 리뷰 테이블 조회
def get_review():
    connection = get_db_connection()
    cursor = connection.cursor(pymysql.cursors.DictCursor)
    
    try:
        sql = "SELECT * FROM review"
        cursor.execute(sql)
        products = cursor.fetchall()  
        
        return products
    finally:
        cursor.close()
        connection.close()

# 감정분석 대기중 리뷰 조회
def get_pending_review():
    connection = get_db_connection()
    cursor = connection.cursor(pymysql.cursors.DictCursor)
    
    try:
        sql = "SELECT review_id, content, sentiment FROM review WHERE sentiment = %s"
        cursor.execute(sql, ('pending',))
        pending_reviews = cursor.fetchall()  
        
        return pending_reviews
    finally:
        cursor.close()
        connection.close()

# 감정 분석된 리뷰를 DB에 업데이트
def update_pending_review(review_id, sentiment):
    connection = get_db_connection()
    cursor = connection.cursor(pymysql.cursors.DictCursor)
    
    try:
        sql = "UPDATE review SET sentiment = %s WHERE review_id = %s"
        cursor.execute(sql, (sentiment, review_id))
        connection.commit() 
    finally:
        cursor.close()
        connection.close()