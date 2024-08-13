package com.no1.book.dao.order;

import com.no1.book.domain.order.OrderDto;
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
import java.util.Map;

//import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("주문 테스트")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    // 모든 테스트 메서드 실행 전 주문 전체 삭제
    @BeforeEach
    void deleteAllOrder() {
        orderDao.deleteAllOrder();
        assertEquals(countAllOrder(), 0);
    }

    @DisplayName("주문 생성 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void createOrder(int ordId) {
        OrderDto orderDto = new OrderDto(1, "주문완료", "301", "Y", "배송 메시지", 25000, 2500, 0, 22500, null, "1", "1");
        orderDao.createOrder(orderDto);
        assertEquals(countAllOrder(), 1);
    }

    @DisplayName("주문상태 업데이트 테스트")
    @Test
    void updateOrderStatus() {
        // 주문 생성
        OrderDto orderDto = new OrderDto(1, "주문완료", "301", "Y", "배송 메시지", 25000, 2500, 0, 22500, null, "1", "1");
        orderDao.createOrder(orderDto);
        assertEquals(countAllOrder(), 1);

        // 상태 업데이트
        Map param = new HashMap();
        param.put("ordId", orderDto.getOrdId());
        param.put("ordStusCode", "결제완료");
        param.put("codeType", "301");
        param.put("upId", orderDto.getRegId());

        orderDao.updateOrderStatus(param);

        // 검증
        assertEquals(orderDao.getOrder(1).getOrdStusCode(), "결제완료");
    }

    // 회원의 주문 전체 개수
    int countCustomerOrders(int custId) {
        return orderDao.getCustomerOrders(custId).size();
    }

    // 주문 전체 개수
    int countAllOrder() {
        return orderDao.getAllOrder().size();
    }
}