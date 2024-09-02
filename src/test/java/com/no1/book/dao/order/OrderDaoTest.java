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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    @Test
    void createOrder() {
        OrderDto orderDto = getTestOrderDto();
        orderDao.createOrder(orderDto);
        assertEquals(countAllOrder(), 1);
    }

    @DisplayName("주문상태 업데이트 테스트")
    @Test
    void updateOrderStatus() {
        // 주문 생성
        OrderDto orderDto = getTestOrderDto();
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
        assertEquals(orderDao.getOrder(orderDto.getOrdId()).getOrdStusCode(), "결제완료");
    }

    // 회원의 주문 전체 개수
    int countCustomerOrders(String custId) {
        return orderDao.getCustomerOrders(custId).size();
    }

    // 주문 전체 개수
    int countAllOrder() {
        return orderDao.getAllOrder().size();
    }

    // 주문 번호 생성
    public synchronized String orderNumGenerator() {
        return UUID.randomUUID().toString();
    }

    // order date
    public String getNow() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);
        return date;
    }

    private OrderDto getTestOrderDto() {
        String custId = "tester";
        return OrderDto.builder()
                .ordId(orderNumGenerator())
                .custId(custId)
                .custChk("Y")
                .pwd("")
                .ordStusCode("RCVD")
                .codeType("301")
                .ordReqMsg("메세지")
                .ordDate(getNow())
                .totalProdPrice(25000)
                .totalDiscPrice(2500)
                .totalPayPrice(22500)
                .regId("1")
                .upId("1")
                .build();
    }
}