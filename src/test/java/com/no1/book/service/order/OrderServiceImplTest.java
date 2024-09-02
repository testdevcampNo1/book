package com.no1.book.service.order;

import com.no1.book.common.exception.order.OrderValidatorErrorMessage;
import com.no1.book.common.exception.order.SystemException;
import com.no1.book.dao.order.OrderDao;
import com.no1.book.dao.order.OrderProductDao;
import com.no1.book.dao.order.OrderStatusHistoryDao;
import com.no1.book.domain.order.OrderDto;
import com.no1.book.domain.order.OrderFormDto;
import com.no1.book.domain.order.OrderProductDto;
import com.no1.book.domain.order.OrderStatusHistoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("OrderService 테스트")
class OrderServiceImplTest {

    @InjectMocks
    OrderServiceImpl orderServiceImpl;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderProductDao orderProductDao;

    @Mock
    private OrderStatusHistoryDao orderStatusHistoryDao;

    // 각 테스트 메서드가 실행되기 전 선행적으로 실행되는 메서드
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("주문 생성 성공 테스트")
    @Test
    void testSaveOrderSuccess() {
        OrderFormDto orderFormDto = getOrderFormDto();

        // 주문 DB 저장
        doNothing().when(orderDao).createOrder(any(OrderDto.class));

        // 예외 미발생 테스트
        assertDoesNotThrow(() -> orderServiceImpl.saveOrder(orderFormDto));
    }

    @DisplayName("주문 생성 실패 테스트")
    @Test
    void testSaveOrderThrowsException() {
        OrderFormDto orderFormDto = getOrderFormDto();

        // DB 저장 실패 예외 던지기
        doThrow(new DataAccessException(OrderValidatorErrorMessage.SALE_PRICE_EXCEEDS_BASE_PRICE.getMessage()) {
        }).when(orderDao).createOrder(any(OrderDto.class));

        // 예외 발생 테스트
        assertThrows(SystemException.class, () -> orderServiceImpl.saveOrder(orderFormDto));
    }

    @DisplayName("주문상품 저장 성공 테스트")
    @Test
    void testSaveOrderProductSuccess() {
        when(orderDao.getAllOrder()).thenReturn(getOrderList());
        when(orderDao.getOrder("")).thenReturn(getOrderDto());

        // 주문 상품 DB 저장
        doNothing().when(orderProductDao).insertOrderProduct(any(OrderProductDto.class));

        // 예외 미발생 테스트
        assertDoesNotThrow(() -> orderServiceImpl.saveOrderProduct("", getOrderProductDtoList()));
    }

    @DisplayName("주문상품 저장 실패 테스트")
    @Test
    void testSaveOrderProductThrowsException() {
        when(orderDao.getAllOrder()).thenReturn(getOrderList());
        when(orderDao.getOrder("")).thenReturn(getOrderDto());

        // DB 저장 실패 예외 던지기
        doThrow(new DataAccessException(OrderValidatorErrorMessage.ACCESS_DATABASE_FAILED.getMessage()) {
        }).when(orderProductDao).insertOrderProduct(any(OrderProductDto.class));

        // 예외 발생 테스트
        assertThrows(SystemException.class, () -> orderServiceImpl.saveOrderProduct("", getOrderProductDtoList()));
    }

    @DisplayName("주문상태이력 저장 성공 테스트")
    @Test
    void testSaveOrderStatusHistorySuccess() {
        when(orderDao.getAllOrder()).thenReturn(getOrderList());
        when(orderDao.getOrder("")).thenReturn(getOrderDto());

        // 주문상태이력 DB 저장
        doNothing().when(orderStatusHistoryDao).createOrderStatusHistory(any(OrderStatusHistoryDto.class));

        // 예외 미발생 테스트
        assertDoesNotThrow(() -> orderServiceImpl.saveOrderStatus(""));
    }

    @DisplayName("주문상태이력 저장 실패 테스트")
    @Test
    void testSaveOrderStatusHistoryThrowsException() {
        when(orderDao.getAllOrder()).thenReturn(getOrderList());
        when(orderDao.getOrder("")).thenReturn(getOrderDto());

        // DB 저장 실패 예외 던지기
        doThrow(new DataAccessException(OrderValidatorErrorMessage.ACCESS_DATABASE_FAILED.getMessage()) {
        }).when(orderStatusHistoryDao).createOrderStatusHistory(any(OrderStatusHistoryDto.class));

        // 예외 발생 테스트
        assertThrows(SystemException.class, () -> orderServiceImpl.saveOrderStatus(""));
    }

    // test order list
    private List<OrderDto> getOrderList() {
        List<OrderDto> orders = new ArrayList<>();

        OrderDto orderDto = OrderDto.builder()
                .ordId(UUID.randomUUID().toString())
                .custId("1")
                .custChk("Y")
                .pwd("")
                .ordStusCode("RCVD")
                .codeType("301")
                .ordReqMsg("message")
                .ordDate("2024-08-18 16:04:30")
                .totalProdPrice(25000)
                .totalDiscPrice(2500)
                .dlvPrice(0)
                .totalPayPrice(22500)
                .regId("1")
                .upId("1")
                .build();

        OrderDto orderDto2 = OrderDto.builder()
                .ordId(UUID.randomUUID().toString())
                .custId("")
                .custChk("Y")
                .pwd("")
                .ordStusCode("RCVD")
                .codeType("301")
                .ordReqMsg("message")
                .ordDate("2024-08-18 16:04:30")
                .totalProdPrice(50000)
                .totalDiscPrice(5000)
                .dlvPrice(2500)
                .totalPayPrice(47500)
                .regId("1")
                .upId("1")
                .build();

        orders.add(orderDto);
        orders.add(orderDto2);

        return orders;
    }

    // test order dto
    private OrderDto getOrderDto() {
        OrderDto orderDto = OrderDto.builder()
                .ordId(UUID.randomUUID().toString())
                .custId("1")
                .custChk("Y")
                .pwd("")
                .ordStusCode("RCVD")
                .codeType("301")
                .ordReqMsg("message")
                .ordDate("2024-08-18 16:04:30")
                .totalProdPrice(25000)
                .totalDiscPrice(2500)
                .dlvPrice(0)
                .totalPayPrice(22500)
                .regId("1")
                .upId("1")
                .build();

        return orderDto;
    }

    // test order form
    OrderFormDto getOrderFormDto() {
        OrderFormDto orderFormDto = OrderFormDto.builder()
                .custId("1")
                .productList(getOrderProductDtoList())
                .ordId(UUID.randomUUID().toString())
                .name("jiseon")
                .orderRequestMessage("message")
                .pwd("")
                .isAllEbook("N")
                .isAllDawnDelivery("N")
                .dlvDate("24시간 이내 도착 예정입니다.")
                .defaultChk("N")
                .email("tester@test.com")
                .addressName("home")
                .telNum("123456789")
                .zipCode("12345")
                .mainAddress("seoul")
                .detailAddress("place A")
                .commonEntrancePassword("#1234")
                .paymentMethod("tossPay")
                .build();

        return orderFormDto;
    }

    // test product
    List<OrderProductDto> getOrderProductDtoList() {
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();

        OrderProductDto orderProductDto = OrderProductDto.builder()
                .ordProdId(340)
                .ordId("")
                .prodId("143")
                .ordChkCode("AVBL")
                .codeType("301")
                .isEbook("Y")
                .dawnDeliChk("N")
                .prodName("폴바셋 ebook")
                .img("")
                .prodPageLink("")
                .ordQty(5)
                .prodBasePrice(30000)
                .totalProdPrice(5 * 30000)
                .discPrice(5000)
                .totalDiscPrice(5 * 5000)
                .totalPayPrice(5 * 30000 - 5 * 5000)
                .regId("1")
                .upId("1")
                .build();

        OrderProductDto orderProductDto2 = OrderProductDto.builder()
                .ordProdId(340)
                .ordId("")
                .prodId("143")
                .ordChkCode("AVBL")
                .codeType("301")
                .isEbook("Y")
                .dawnDeliChk("N")
                .prodName("폴바셋 ebook")
                .img("")
                .prodPageLink("")
                .ordQty(5)
                .prodBasePrice(30000)
                .totalProdPrice(5 * 30000)
                .discPrice(5000)
                .totalDiscPrice(5 * 5000)
                .totalPayPrice(5 * 30000 - 5 * 5000)
                .regId("1")
                .upId("1")
                .build();

        orderProductDtoList.add(orderProductDto);
        orderProductDtoList.add(orderProductDto2);
        return orderProductDtoList;
    }
}