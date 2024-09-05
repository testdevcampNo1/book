

def get_api_routes():
    routes = {
        "회원": {
            "회원가입 화면": {"method": "GET", "url": "/customer/signup"},
            "회원가입 처리": {"method": "POST", "url": "/customer/signup"},
            "로그인 처리": {"method": "POST", "url": "/customer/login"},
            "아이디 중복 조회": {"method": "GET", "url": "/customer/{custId}"},
            "회원정보수정": {"method": "POST", "url": "/customer/edit/{custId}"}
        },
        "상품": {
            "상품 리스트 조회": {"method": "GET", "url": "/product/list"},
            "상품 상세 페이지": {"method": "GET", "url": "/product/detail/{prodId}"},
            "상품 관리 페이지": {"method": "GET", "url": "/product/manage"},
            "상품 등록": {"method": "POST", "url": "/product/manage/add"},
            "상품 조회": {"method": "POST", "url": "/product/manage/view"},
            "상품 업데이트": {"method": "POST", "url": "/product/manage/update"},
            "상품 삭제": {"method": "POST", "url": "/product/manage/delete"},
            "리뷰 등록": {"method": "POST", "url": "/review/add/{prodId}"},
            "리뷰 조회": {"method": "GET", "url": "/review/list/{prodId}"},
            "리뷰 수정": {"method": "PUT", "url": "/review/update/{reviewId}"},
            "리뷰 삭제": {"method": "DELETE", "url": "/review/delete/{reviewId}"}
        },
        "장바구니": {
            "장바구니 조회": {"method": "GET", "url": "/cart/list"},
            "장바구니 추가": {"method": "POST", "url": "/cart/{custId}"},
            "장바구니 삭제": {"method": "GET", "url": "/cart/{custId}"},
            "장바구니 수량": {"method": "POST", "url": "/cart/{custId}"}
        },
        "주문": {
            "주문 신청": {"method": "POST", "url": "/order/form"},
            "주문 완료": {"method": "POST", "url": "/order/complete"},
            "주문 내역": {"method": "GET", "url": "/order/history"},
            "주문 취소": {"method": "POST", "url": "/order/cancel"}
        },
        "결제": {
            "결제 신청": {"method": "POST", "url": "/payment/request"},
            "결제 완료": {"method": "GET", "url": "/payment/complete"}
        },
        "고객센터": {
            "고객센터 메인 페이지 조회": {"method": "GET", "url": "/cscenter"}
        },
        "공지게시판": {
            "공지목록 조회": {"method": "GET", "url": "/cscenter/notice/list"},
            "공지 게시글 조회": {"method": "GET", "url": "/cscenter/notice/{notcNum}"},
            "공지 등록 폼 이동": {"method": "GET", "url": "/cscenter/notice"},
            "공지 등록": {"method": "POST", "url": "/cscenter/notice"},
            "공지 수정": {"method": "POST", "url": "/cscenter/notice/modify"},
            "공지 삭제": {"method": "GET", "url": "/cscenter/notice/remove"}
        },
        "FAQ게시판": {
            "FAQ 목록 조회": {"method": "GET", "url": "/cscenter/faq/list"},
            "FAQ 등록 폼 이동": {"method": "GET", "url": "/cscenter/faq/write"},
            "FAQ 등록": {"method": "POST", "url": "/cscenter/faq/write"},
            "FAQ 수정": {"method": "POST", "url": "/cscenter/faq/modify"},
            "FAQ 삭제": {"method": "GET", "url": "/cscenter/faq/remove"}
        }
    }
    return routes