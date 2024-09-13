

def get_api_routes():
    routes = {
        "회원": {
            "회원가입 화면": {"method": "GET", "url": "/customer/signup"}
        },
        "상품": {
            "상품 리스트 조회": {"method": "GET", "url": "/product/list"},
            "상품 상세 페이지": {"method": "GET", "url": "/product/detail/{prodId}"},
            "상품 관리 페이지": {"method": "GET", "url": "/product/manage"}
        },
        "장바구니": {
            "장바구니 조회": {"method": "GET", "url": "/cart/list"}
        },
        "주문": {
            "주문 내역": {"method": "GET", "url": "/order/history"}
        },
        "고객센터": {
            "고객센터 메인 페이지 조회": {"method": "GET", "url": "/cscenter"}
        },
        "공지게시판": {
            "공지목록 조회": {"method": "GET", "url": "/cscenter/notice/list"}
        },
        "FAQ게시판": {
            "FAQ 목록 조회": {"method": "GET", "url": "/cscenter/faq/list"}
        }
    }
    return routes