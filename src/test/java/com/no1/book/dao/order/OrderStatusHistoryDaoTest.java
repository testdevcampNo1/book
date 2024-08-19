package com.no1.book.dao.order;

import com.no1.book.domain.order.OrderDto;
import com.no1.book.domain.order.OrderStatusHistoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//import static org.junit.Assert.*;

@DisplayName("주문상태이력 테스트")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class OrderStatusHistoryDaoTest {

    @Autowired
    private OrderStatusHistoryDao orderStatusHistoryDao;

    @Autowired
    private OrderDao orderDao;

    // 모든 테스트 메서드 실행 전 주문 및 주문상품 전체 삭제, 주문 생성
    @BeforeEach
    void init() {
        orderDao.deleteAllOrder();
        assertEquals(countAllOrder(), 0);
        orderStatusHistoryDao.deleteAllOrderStatusHistory();
        assertEquals(countAllOrderStatusHistory(), 0);

        OrderDto orderDto = getTestOrderDto();
        orderDao.createOrder(orderDto);
        assertEquals(countAllOrder(), 1);
    }

    @DisplayName("주문상태이력 추가 테스트")
    @Test
    void insertOrderStatusHistory() {
        String ordId = orderDao.getAllOrder().get(0).getOrdId();
        OrderStatusHistoryDto orderStatusHistoryDto = new OrderStatusHistoryDto(ordId, null, "주문완료", null, "1", "1");
        orderStatusHistoryDao.createOrderStatusHistory(orderStatusHistoryDto);
        assertEquals(countAllOrderStatusHistory(), 1);
    }

    @DisplayName("주문상태이력 일련번호로 조회")
    @Test
    void getOrderStatusHistory() {
        String ordId = orderDao.getAllOrder().get(0).getOrdId();
        // 주문상태이력 추가
        OrderStatusHistoryDto orderStatusHistoryDto = new OrderStatusHistoryDto(ordId, null, "주문완료", null, "1", "1");
        orderStatusHistoryDao.createOrderStatusHistory(orderStatusHistoryDto);
        assertEquals(countAllOrderStatusHistory(), 1);

        // 조회
        OrderStatusHistoryDto savedOrderStatusHistoryDto = orderStatusHistoryDao.getAllOrderStatusHistory().get(0);
        orderStatusHistoryDao.getOrderStatusHistory(savedOrderStatusHistoryDto.getOrdStusHistorySeq());
        assertNotNull(orderStatusHistoryDao.getOrderStatusHistory(savedOrderStatusHistoryDto.getOrdStusHistorySeq()));
    }

    @DisplayName("주문상태이력 주문번호로 조회")
    @Test
    void getOrderStatusHistoryByOrdId() {
        String ordId = orderDao.getAllOrder().get(0).getOrdId();
        // 주문상태이력 추가
        OrderStatusHistoryDto orderStatusHistoryDto = new OrderStatusHistoryDto(ordId, null, "주문완료", null, "1", "1");
        orderStatusHistoryDao.createOrderStatusHistory(orderStatusHistoryDto);
        assertEquals(countAllOrderStatusHistory(), 1);

        // 조회
        List<OrderStatusHistoryDto> list = orderStatusHistoryDao.getOrderStatusHistoryByOrdId(ordId);
        assertNotNull(list);
        assertEquals(countAllOrderStatusHistory(), 1);
    }

    @DisplayName("주문상태이력 회원ID로 조회")
    @Test
    void getCustomerOrderStatusHistory() {
        String ordId = orderDao.getAllOrder().get(0).getOrdId();
        // 주문상태이력 추가
        OrderStatusHistoryDto orderStatusHistoryDto = new OrderStatusHistoryDto(ordId, null, "주문완료", null, "1", "1");
        orderStatusHistoryDao.createOrderStatusHistory(orderStatusHistoryDto);
        assertEquals(countAllOrderStatusHistory(), 1);

        // 조회
        List<OrderStatusHistoryDto> list = orderStatusHistoryDao.getCustomerOrderStatusHistory(1);
        assertNotNull(list);
        assertEquals(countAllOrderStatusHistory(), 1);
    }

    @DisplayName("주문상태이력 주문ID로 삭제")
    @Test
    void deleteOrderStatusHistory() {
        String ordId = orderDao.getAllOrder().get(0).getOrdId();
        // 주문상태이력 추가
        OrderStatusHistoryDto orderStatusHistoryDto = new OrderStatusHistoryDto(ordId, null, "주문완료", null, "1", "1");
        orderStatusHistoryDao.createOrderStatusHistory(orderStatusHistoryDto);
        assertEquals(countAllOrderStatusHistory(), 1);

        // 삭제
        orderStatusHistoryDao.deleteOrderStatusHistory(ordId);
        assertEquals(countOrderStatusHistory(ordId), 0);
    }

    @DisplayName("주문상태이력 주문ID로 삭제")
    @Test
    void deleteAllOrderStatusHistory() {
        String ordId = orderDao.getAllOrder().get(0).getOrdId();
        // 주문상태이력 추가
        OrderStatusHistoryDto orderStatusHistoryDto = new OrderStatusHistoryDto(ordId, null, "주문완료", null, "1", "1");
        orderStatusHistoryDao.createOrderStatusHistory(orderStatusHistoryDto);
        assertEquals(countAllOrderStatusHistory(), 1);

        // 삭제
        orderStatusHistoryDao.deleteAllOrderStatusHistory();
        assertEquals(countAllOrderStatusHistory(), 0);
    }

    // 주문상태이력 전체 개수
    int countAllOrderStatusHistory() {
        return orderStatusHistoryDao.getAllOrderStatusHistory().size();
    }

    // 한 주문의 주문상태이력 개수
    int countOrderStatusHistory(String ordId) {
        return orderStatusHistoryDao.getOrderStatusHistoryByOrdId(ordId).size();
    }

    // 주문 전체 개수
    int countAllOrder() {
        return orderDao.getAllOrder().size();
    }

    // 주문 번호 생성
    public synchronized String orderNumGenerator() {
        try {
            Thread.sleep(1);
            LocalDateTime srcTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSS");
            return srcTime.format(formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // order date
    public String getNow() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);
        return date;
    }

    private OrderDto getTestOrderDto() {
        return OrderDto.builder()
                .ordId(orderNumGenerator())
                .custId(1)
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