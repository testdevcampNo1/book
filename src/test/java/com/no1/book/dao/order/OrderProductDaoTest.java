package com.no1.book.dao.order;

import com.no1.book.domain.order.OrderDto;
import com.no1.book.domain.order.OrderProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("주문상품 테스트")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class OrderProductDaoTest {

    @Autowired
    private OrderProductDao orderProductDao;

    @Autowired
    private OrderDao orderDao;

    // 모든 테스트 메서드 실행 전 주문 및 주문상품 전체 삭제, 주문 생성
    @BeforeEach
    void init() {
        orderDao.deleteAllOrder();
        assertEquals(countAllOrder(), 0);
        orderProductDao.deleteAllOrderProduct();
        assertEquals(countAllOrderProduct(), 0);

        OrderDto orderDto = new OrderDto(1, 1, "주문완료", "301", "Y", "배송 메시지", 25000, 2500, 0, 22500, null, "1", "1");
        orderDao.createOrder(orderDto);
        assertEquals(countAllOrder(), 1);
    }

    @DisplayName("주문상품 추가 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void insertOrderProduct(int ordProdId) {
        orderProductDao.insertOrderProduct(getOrderProduct(ordProdId, 1, "1"));
        assertEquals(countAllOrderProduct(), 1);
    }

    @DisplayName("주문상품상태 업데이트 테스트")
    @Test
    void updateOrderProductStatus() {
        // 주문상품 추가
        OrderProductDto orderProductDto = getOrderProduct(1, 1, "1");
        orderProductDao.insertOrderProduct(orderProductDto);
        assertEquals(countAllOrderProduct(), 1);

        // 상태 업데이트
        Map param = new HashMap();
        param.put("ordProdId", orderProductDto.getOrdProdId());
        param.put("ordProdStusCode", "반품접수");
        param.put("upId", orderProductDto.getRegId());

        orderProductDao.updateOrderProductStatus(param);

        // 검증
        OrderProductDto updatedOrderProductDto = orderProductDao.getOrderProduct(orderProductDto.getOrdProdId());
        assertEquals("반품접수", updatedOrderProductDto.getOrdProdStusCode());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @DisplayName("주문상품 조회 테스트 - BY OrderProductId")
    void getOrderProduct(int ordProdId) {
        // 추가
        for(int i = 1; i<11; i++) {
            OrderProductDto orderProductDto = getOrderProduct(i, 1, "1");
            orderProductDao.insertOrderProduct(orderProductDto);
        }

        // 조회
        orderProductDao.getOrderProduct(ordProdId);
        assertEquals(countAllOrderProduct(), 10);
    }

    @Test
    @DisplayName("회원의 주문상품 조회 테스트")
    void getCustomerOrderProducts() {
        // 회원2의 주문2 생성
        OrderDto orderDto2 = new OrderDto(2, 2, "주문완료", "301", "Y", "배송 메시지", 25000, 2500, 0, 22500, null, "1", "1");
        orderDao.createOrder(orderDto2);
        // init에서 주문1 생성했으므로 총 주문 2개
        assertEquals(countAllOrder(), 2);

        // 주문2에 주문상품 추가
        for(int i = 1; i<11; i++) {
            OrderProductDto orderProductDto = getOrderProduct(i, 2, "1");
            orderProductDao.insertOrderProduct(orderProductDto);
        }

        // 회원2의 주문상품 조회
        List<OrderProductDto> list = orderProductDao.getCustomerOrderProducts(2);
        assertEquals(list.size(), 10);
    }

    @Test
    @DisplayName("한 주문의 주문상품 조회 테스트")
    void getOrderProductsByOrderId() {
        // 주문1에 주문상품 추가
        for(int i = 1; i<11; i++) {
            OrderProductDto orderProductDto = getOrderProduct(i, 1, "1");
            orderProductDao.insertOrderProduct(orderProductDto);
        }

        // 주문1의 주문상품 조회
        List<OrderProductDto> list = orderProductDao.getOrderProductsByOrderId(1);
        assertEquals(list.size(), 10);
    }

    // 한 주문의 주문상품 전체 개수
    int countOrderProductByOrderId(int orderId) {
        return orderProductDao.getOrderProductsByOrderId(orderId).size();
    }

    // 회원의 주문상품 전체 개수
    int countCustomerAllOrderProduct(int custId) {
        return orderProductDao.getCustomerOrderProducts(custId).size();
    }

    // 주문상품 전체 개수
    int countAllOrderProduct() {
        return orderProductDao.getAllOrderProduct().size();
    }

    // 주문 전체 개수
    int countAllOrder() {
        return orderDao.getAllOrder().size();
    }

    // 테스트용 OrderProductDto 반환
    OrderProductDto getOrderProduct(int ordProdId, int ordId, String prodId) {
        OrderProductDto orderProductDto = new OrderProductDto(ordProdId, ordId, prodId, "주문가능", "301", "N", "자바의 정석", 3, "img", "google.com", "N", 75000, 0, 75000, "1", "1");
        return orderProductDto;
    }
}