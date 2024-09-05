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
        sql = "SELECT prod_name, prod_id, sale_price, total_sales, star_avg, dawn_deli_chk, ord_chk_code FROM product"
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
        sql = "SEECT * FROM boardFAQ"
        cursor.execute(sql)
        products = cursor.fetchall()  # DB에서 가져온 결과
        
        return products
    finally:
        cursor.close()
        connection.close()
        
# 공지사항 게시판 조회
def get_notice():
    connection = get_db_connection()
    cursor = connection.cursor(pymysql.cursors.DictCursor) 
    
    try:
        sql = "SEECT * FROM boardNotice"
        cursor.execute(sql)
        products = cursor.fetchall()  # DB에서 가져온 결과
        
        return products
    finally:
        cursor.close()
        connection.close()

# QNA 게시판 조회
def get_QNA():
    connection = get_db_connection()
    cursor = connection.cursor(pymysql.cursors.DictCursor)
    
    try:
        sql = "SEECT * FROM boardQNA"
        cursor.execute(sql)
        products = cursor.fetchall()  # DB에서 가져온 결과
        
        return products
    finally:
        cursor.close()
        connection.close()