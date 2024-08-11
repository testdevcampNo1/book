package com.no1.book.service.order;

import com.no1.book.dao.order.OrderDao;
import com.no1.book.dao.order.OrderProductDao;
import com.no1.book.dao.order.OrderStatusHistoryDao;
import com.no1.book.domain.order.OrderDto;
import com.no1.book.domain.order.OrderFormDto;
import com.no1.book.domain.order.OrderProductDto;
import com.no1.book.domain.order.OrderStatusHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderProductDao orderProductDao;

    @Autowired
    private OrderStatusHistoryDao orderStatusHistoryDao;

    private OrderFormDto orderFormDto = new OrderFormDto();
    private List<OrderProductDto> orderProductDtoList;

    /*
    # 주문 화면 진입
    1. 주문 정보 삽입

    # 주문 버튼 클릭
    1. 주문 정보 검증
    2. 결제 요청
    3. DB 저장 - 주문, 주문상품, 주문상태, 배송, 결제
    4. 주문 완료 페이지로 redirect
     */

    // 주문 정보 삽입
    public void initOrderInfo(int custId) {
        orderFormDto.setCustId(custId);
        // 상품, 금액, isAllEbook, isAllDawnDelivery
        testOrderProductList();
        // 고객 정보 - email, name, telNum
        insertCustInfo();
        // 배송 정보 - zipCode, mainAddress, detailAddress, commonEntrancePassword
        insertAddressInfo();
        // 테스트용 결제 수단
        orderFormDto.setPaymentMethod("애플페이");
    }

    // TODO: 입력받은 데이터 삽입 - param 저장

    // 주문자 정보 검증
    private void validateOrderer() {
        if(orderFormDto.getEmail() == null || orderFormDto.getEmail().isEmpty() || orderFormDto.getAddressName() == null || orderFormDto.getAddressName().isEmpty() || orderFormDto.getTelNum() == null || orderFormDto.getTelNum().isEmpty()) {
            throw new IllegalArgumentException("이메일 또는 이름 또는 전화번호가 존재하지 않습니다.");
        }

        // 비회원일 경우 주문 조회 비밀번호 검증
        if(orderFormDto.getCustId() == -1 && (orderFormDto.getPwd() == null || orderFormDto.getPwd().isEmpty())) {
            throw new IllegalArgumentException("비회원은 주문 조회 비밀번호가 필요합니다.");
        }
    }

    // 배송지 정보 검증
    private void validateAddress() {
        if(orderFormDto.getZipCode() == null || orderFormDto.getZipCode().isEmpty() || orderFormDto.getMainAddress() == null || orderFormDto.getMainAddress().isEmpty() || orderFormDto.getDetailAddress() == null || orderFormDto.getDetailAddress().isEmpty()) {
            throw new IllegalArgumentException("배송지 정보가 존재하지 않습니다.");
        }
    }

    // 금액 검증
    private void validateOrderPrice() {
        if(orderFormDto.getTotalProdBasePrice() < 0 || orderFormDto.getTotalDiscPrice() < 0 || orderFormDto.getTotalSalePrice() < 0 || orderFormDto.getDlvPrice() < 0) {
            throw new IllegalArgumentException("음수인 금액이 존재합니다.");
        }

        if(orderFormDto.getTotalProdBasePrice() < orderFormDto.getTotalSalePrice()) {
            throw new IllegalArgumentException("정가의 합보다 판매가의 합이 더 큽니다.");
        }

        if(orderFormDto.getTotalSalePrice() < orderFormDto.getTotalDiscPrice()) {
            throw new IllegalArgumentException("판매가의 합보다 할인가의 합이 더 큽니다.");
        }
    }

    // 주문상품 검증
    private void validateOrderProduct() {
        // 주문 상품 리스트 검증
        if(orderProductDtoList == null || orderProductDtoList.isEmpty()) {
            throw new IllegalArgumentException("주문 상품 목록이 존재하지 않습니다.");
        }

        // 각 주문 상품 검증
        for(OrderProductDto product : orderProductDtoList) {
            // 상품id 검증
            if(product.getProdId() == null || product.getProdId().isEmpty()) {
                throw new IllegalArgumentException("주문 상품 id가 존재하지 않습니다.");
            }

            // 상품 상태 검증
            if(product.getOrdProdStusCode() == null || product.getOrdProdStusCode().isEmpty() || product.getOrdProdStusCode() == "주문불가" || product.getCodeType() == null || product.getCodeType().isEmpty()) {
                throw new IllegalArgumentException("주문 불가능한 상품이 존재합니다.");
            }

            // 0개 이하인 주문 수량 검증
            if(product.getOrdQty() <= 0) {
                throw new IllegalArgumentException("주문 수량이 0개 이하인 상품이 존재합니다.");
            }

            // 재고 부족 검증
            // 재고 DB에서 실시간으로 조회 필요
            if(getProductStockCount(product.getProdId()) < product.getOrdQty()) {
                throw new IllegalArgumentException("재고가 부족한 상품이 존재합니다.");
            }

            // 상품 금액 검증
            if(product.getTotalProdPrice() < 0 || product.getTotalDiscPrice() < 0 || product.getTotalPayPrice() < 0) {
                throw new IllegalArgumentException("음수인 금액이 존재합니다.");
            }

            if(product.getTotalProdPrice() < product.getTotalPayPrice()) {
                throw new IllegalArgumentException("정가의 합보다 판매가의 합이 더 큽니다.");
            }

            if(product.getTotalPayPrice() < product.getTotalDiscPrice()) {
                throw new IllegalArgumentException("판매가의 합보다 할인가의 합이 더 큽니다.");
            }
        }
    }

    // 결제 정보 검증
    private void validatePaymentMethod() {
        if(orderFormDto.getPaymentMethod() == null || orderFormDto.getPaymentMethod().isEmpty()) {
            throw new IllegalArgumentException("결제 수단이 존재하지 않습니다.");
        }
    }

    // 결제 요청
    boolean requestPayment() {
        // 결제시 필요한 데이터 넘기기
        // try catch로 결제 요청
        // 결제 성공시 DB에 저장 - 주문 -> 결제, 주문상품, 주문상태, 배송
        // 나머지 table이 주문id를 필요로 하기 때문에, 주문이 최우선순위로 DB에 저장되어야 한다.
        // paymentDao.requestPayment();
        // TODO: 결제 성공 여부로 변경 필요
        return true;
    }

    // DB 저장
    // 주문 저장
    @Override
    public void saveOrder() {
        // TODO: ordId 일련번호로 수정해야 할까?
        // TODO: ordDate sql에서 현재 시간으로 저장하도록 변경 필요
        // TODO: 실제 데이터로 변경 필요
        // TODO: 예외 처리
        int custId = orderFormDto.getCustId();
        String custCheck = custId > -1 ? "Y" : "N";

        OrderDto orderDto = new OrderDto(1, custId, "결제완료", "301", custCheck, orderFormDto.getOrderRequestMessage(), orderFormDto.getTotalProdBasePrice(), orderFormDto.getTotalDiscPrice(), orderFormDto.getDlvPrice(), orderFormDto.getTotalSalePrice(), orderFormDto.getPwd(), getRegId(), getRegId());

        try {
            orderDao.createOrder(orderDto);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    // 주문상품 저장
    @Override
    public void saveOrderProduct() {
        // 가장 최근 주문의 주문id 조회 필요 - 주문목록 최신순 정렬
        // TODO: 예외 처리
        try {
            for(OrderProductDto product : orderProductDtoList) {
                product.setOrdId(getOrderId());
                orderProductDao.insertOrderProduct(product);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    // 주문상태 저장
    @Override
    public void saveOrderStatus() {
        // 가장 최근 주문의 주문id 조회 필요 - 주문목록 최신순 정렬
        // TODO: 예외 처리
        OrderStatusHistoryDto orderStatusHistoryDto = new OrderStatusHistoryDto(getOrderId(), null, "결제완료", "결제가 승인됨", getRegId(), getRegId());
        try {
            orderStatusHistoryDao.createOrderStatusHistory(orderStatusHistoryDto);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    // 배송 저장
    @Override
    public void saveDelivery() {
        // 배송 Dao 구현 필요
        // TODO: 예외 처리
    }

    // 결제 저장
    @Override
    public void savePayment() {
        // 구현된 결제 Dao 필요
        // TODO: 예외 처리
    }

    // 가장 최근 주문의 주문id 조회
    @Override
    public int getOrderId() {
        return orderDao.getAllOrder().get(0).getOrdId();
    }

    // 고객 정보 삽입 - email, name, telNum
    @Override
    public void insertCustInfo() {
        // TODO: 실제 data 삽입 필요, 예외처리
        if(orderFormDto.getCustId() > -1) {
            // 회원일 경우 DB에서 조회하여 저장
            // custDao.get(custId)
        } else {
            // 비회원일 경우 입력받아서 저장
            // test용 비밀번호
            orderFormDto.setPwd("1234");
        }
        orderFormDto.setEmail("test@test.com");
        orderFormDto.setAddressName("학원");
        orderFormDto.setTelNum("01012345678");
    }

    // 배송 정보 삽입 - zipCode, mainAddress, datailAddress
    @Override
    public void insertAddressInfo() {
        // TODO: 배송 table 구현 필요, 예외처리
        if(orderFormDto.getCustId() > -1) {
            // 회원일 경우 DB에서 기본배송지 조회하여 저장
            // deliveryDao.get(custId)
            // 만약 기본배송지 데이터가 없다면 입력받아서 저장 필요
        } else {
            // 비회원일 경우 입력받아서 저장
        }
        orderFormDto.setZipCode("testZipCode");
        orderFormDto.setMainAddress("서울특별시 강남구 역삼동 826-21");
        orderFormDto.setDetailAddress("10C 강의장");
        orderFormDto.setOrderRequestMessage("문 앞에 놔주세요.");
    }

    // regId, upId
    // 회원일 경우 custId를 String으로 변환하여 저장
    // 비회원일 경우 email을 저장
    String getRegId() {
        int custId = orderFormDto.getCustId();
        String regId = custId > -1 ? String.valueOf(custId) : orderFormDto.getEmail();
        return regId;
    }

    // 재고 개수
    int getProductStockCount(String prodId) {
        // 상품id로 재고 개수 조회
        // productDao.getStockCount(prodId);
        // TODO: 조회한 재고 개수로 변경 필요
        return 10;
    }

    // test용 상품목록 삽입
    private void testOrderProductList() {
        // test용 더미데이터
        // order 테이블 아직 생성되기 전이므로 -1로 설정
        OrderProductDto orderProductDto1 = new OrderProductDto(1, -1, "100", null, null, "N", "자바의 정석 3판", 3, ".img", "google.com", "Y", 75000, 7500, 67500, null, null);
        OrderProductDto orderProductDto2 = new OrderProductDto(2, -1, "101", null, null, "N", "자바의 정석 2판", 1, ".img", "google.com", "N", 60000, 0, 60000,  null, null);
        OrderProductDto orderProductDto3 = new OrderProductDto(3, -1, "102", null, null, "N", "데이터모델링", 1, ".img", "google.com", "Y", 35000, 5000, 30000,  null, null);
        OrderProductDto orderProductDto4 = new OrderProductDto(4, -1, "103", null, null, "Y", "SQL 튜닝", 1, ".img", "google.com", "N", 25000, 0, 25000,  null, null);
        OrderProductDto orderProductDto5 = new OrderProductDto(5, -1, "104", null, null, "Y", "토비의 스프링", 1, ".img", "google.com", "N", 30000, 0, 30000, null, null);

        orderProductDtoList.add(orderProductDto1);
        orderProductDtoList.add(orderProductDto2);
        orderProductDtoList.add(orderProductDto3);
        orderProductDtoList.add(orderProductDto4);
        orderProductDtoList.add(orderProductDto5);

        orderFormDto.setProductList(orderProductDtoList);

        // 금액 정보
        int totalProdBasePrice = 0;
        int totalDiscPrice = 0;
        int totalSalePrice = 0;

        // 모든 상품이 새벽배송인가
        /*
        모든 상품이 새벽 배송일 경우 - 24시간 이내 배송 예정
        새벽 배송 아닌 상품이 있을 경우 - 48시간 이내 배송 예정
         */
        boolean isAllDawnDelivery = true;

        // 모든 상품이 ebook인가
        boolean isAllEbook = true;

        // 금액 계산
        for(OrderProductDto product : orderProductDtoList) {
            totalProdBasePrice += product.getTotalProdPrice();
            totalDiscPrice += product.getTotalDiscPrice();
            totalSalePrice += product.getTotalPayPrice();

            if(product.getIsDawnDelivery() == "N") isAllDawnDelivery = false;
            if(product.getEbookChk() == "N") isAllEbook = false;
        }

        orderFormDto.setTotalProdBasePrice(totalProdBasePrice);
        orderFormDto.setTotalDiscPrice(totalDiscPrice);
        orderFormDto.setTotalSalePrice(totalSalePrice);
        orderFormDto.setDlvPrice(0);
        orderFormDto.setIsAllEbook(isAllEbook ? "Y" : "N");
        orderFormDto.setIsAllDawnDelivery(isAllDawnDelivery ? "Y" : "N");
    }
}
