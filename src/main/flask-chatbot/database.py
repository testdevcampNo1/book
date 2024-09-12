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

# ------ 챗봇 시스템 프롬프팅 용도 ------

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
        sql = '''
            SELECT 
                c.item_qty,
                p.prod_name,
                p.total_sales
            FROM no1.cart c
            JOIN no1.product p ON c.prod_id = p.prod_id
            WHERE c.cust_id = %s;
        '''
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
        sql = "SELECT faq_num, faq_title, faq_content FROM boardFAQ"
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
        sql = "SELECT notc_num, notc_title, notc_content FROM boardNotice"
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

def get_order(cust_id):
    connection = get_db_connection()
    cursor = connection.cursor(pymysql.cursors.DictCursor)
    
    try:
        sql = """
        SELECT 
            o.ord_id,
            o.cust_id,
            o.ord_date,
            o.total_pay_price,
            op.prod_id,
            op.prod_name,
            op.ord_qty,
            op.total_prod_price,
            op.total_pay_price
        FROM 
            no1.order o
        JOIN 
            no1.orderProduct op
        ON 
            o.ord_id = op.ord_id
        WHERE 
            o.cust_id = %s;
        """
        cursor.execute(sql, (cust_id,))
        products = cursor.fetchall()  
        
        return products
    finally:
        cursor.close()
        connection.close()

import pymysql
from datetime import datetime

def add_to_cart(cust_id, prod_name):
    connection = get_db_connection()  # DB 연결 함수 호출
    cursor = connection.cursor(pymysql.cursors.DictCursor)
    
    try:
        # 1. product 테이블에서 prod_name을 이용해 prod_id를 조회
        find_prod_id_sql = "SELECT prod_id FROM no1.product WHERE prod_name = %s"
        cursor.execute(find_prod_id_sql, (prod_name,))
        result = cursor.fetchone()
        
        if result:
            prod_id = result['prod_id']
            
            # 2. cart 테이블에 해당 상품을 추가
            insert_cart_sql = """
            INSERT INTO no1.cart (cust_id, prod_id, item_qty, register_date)
            VALUES (%s, %s, %s, %s)
            """
            # 현재 날짜와 시간을 가져오기 (register_date로 사용)
            current_time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
            
            # 기본적으로 수량을 1로 설정
            cursor.execute(insert_cart_sql, (cust_id, prod_id, 1, current_time))
            
            # DB에 변경 사항 적용 (commit)
            connection.commit()
            
            print(f"상품 {prod_name} (prod_id: {prod_id})가 장바구니에 추가되었습니다.")
        else:
            print(f"상품 '{prod_name}'을 찾을 수 없습니다.")
    
    except Exception as e:
        print(f"오류 발생: {e}")
        connection.rollback()  # 에러 발생 시 롤백
    finally:
        cursor.close()
        connection.close()


# ----- 리뷰 감정 분석 관련 ------


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