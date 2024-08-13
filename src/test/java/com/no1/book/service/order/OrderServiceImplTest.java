package com.no1.book.service.order;

import com.no1.book.common.exception.OrderValidatorErrorMessage;
import com.no1.book.common.exception.SystemException;
import com.no1.book.dao.order.OrderDao;
import com.no1.book.dao.order.OrderProductDao;
import com.no1.book.dao.order.OrderStatusHistoryDao;
import com.no1.book.domain.customer.CustomerDto;
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
        when(orderDao.getOrder(1)).thenReturn(getOrderDto());

        // 주문 상품 DB 저장
        doNothing().when(orderProductDao).insertOrderProduct(any(OrderProductDto.class));

        // 예외 미발생 테스트
        assertDoesNotThrow(() -> orderServiceImpl.saveOrderProduct(getOrderProductDtoList()));
    }

    @DisplayName("주문상품 저장 실패 테스트")
    @Test
    void testSaveOrderProductThrowsException() {
        when(orderDao.getAllOrder()).thenReturn(getOrderList());
        when(orderDao.getOrder(1)).thenReturn(getOrderDto());

        // DB 저장 실패 예외 던지기
        doThrow(new DataAccessException(OrderValidatorErrorMessage.SAVE_DATABASE_FAILED.getMessage()) {
        }).when(orderProductDao).insertOrderProduct(any(OrderProductDto.class));

        // 예외 발생 테스트
        assertThrows(SystemException.class, () -> orderServiceImpl.saveOrderProduct(getOrderProductDtoList()));
    }

    @DisplayName("주문상태이력 저장 성공 테스트")
    @Test
    void testSaveOrderStatusHistorySuccess() {
        when(orderDao.getAllOrder()).thenReturn(getOrderList());
        when(orderDao.getOrder(1)).thenReturn(getOrderDto());

        // 주문상태이력 DB 저장
        doNothing().when(orderStatusHistoryDao).createOrderStatusHistory(any(OrderStatusHistoryDto.class));

        // 예외 미발생 테스트
        assertDoesNotThrow(() -> orderServiceImpl.saveOrderStatus());
    }

    @DisplayName("주문상태이력 저장 실패 테스트")
    @Test
    void testSaveOrderStatusHistoryThrowsException() {
        when(orderDao.getAllOrder()).thenReturn(getOrderList());
        when(orderDao.getOrder(1)).thenReturn(getOrderDto());

        // DB 저장 실패 예외 던지기
        doThrow(new DataAccessException(OrderValidatorErrorMessage.SAVE_DATABASE_FAILED.getMessage()) {
        }).when(orderStatusHistoryDao).createOrderStatusHistory(any(OrderStatusHistoryDto.class));

        // 예외 발생 테스트
        assertThrows(SystemException.class, () -> orderServiceImpl.saveOrderStatus());
    }

    @DisplayName("주문ID 조회 성공 테스트")
    @Test
    void testGetOrderIdSuccess() {
        when(orderDao.getAllOrder()).thenReturn(new ArrayList<>());

        int ordId = orderServiceImpl.getOrderId();
        assertEquals(1, ordId);
    }

    @DisplayName("주문ID 조회 실패 테스트")
    @Test
    void testGetOrderIdThrowsException() {
        when(orderDao.getAllOrder()).thenReturn(new ArrayList<>());

        assertThrows(SystemException.class, () -> orderServiceImpl.getOrderId());
    }

    // test order list
    private List<OrderDto> getOrderList() {
        List<OrderDto> orders = new ArrayList<>();
        orders.add(new OrderDto(1, "RCVD", "301", "Y", "Test request", 10000, 1000, 2500, 8750, "password", "1", "1"));
        return orders;
    }

    // test order dto
    private OrderDto getOrderDto() {
        return new OrderDto(1, "RCVD", "301", "Y", "Test request", 10000, 1000, 2500, 8750, "password", "1", "1");
    }

    // test order form
    OrderFormDto getOrderFormDto() {
        OrderFormDto orderFormDto = new OrderFormDto();

        orderFormDto.setCustId(1);
        orderFormDto.setName("tester");
        orderFormDto.setEmail("test@example.com");
        orderFormDto.setTelNum("01012345678");
        orderFormDto.setZipCode("1234");
        orderFormDto.setMainAddress("Main Address");
        orderFormDto.setDetailAddress("Detail Address");
        orderFormDto.setTotalProdBasePrice(10000);
        orderFormDto.setTotalDiscPrice(1000);
        orderFormDto.setDlvPrice(2500);
        orderFormDto.setTotalSalePrice(8750);
        orderFormDto.setProductList(getOrderProductDtoList());
        orderFormDto.setPaymentMethod("카드결제");

        return orderFormDto;
    }

    // test product
    List<OrderProductDto> getOrderProductDtoList() {
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        OrderProductDto orderProductDto = new OrderProductDto(1, -1, "100", "AVBL", "301", "N", "[국내도서] Java의 정석:기초편 세트", 2, "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788994492049.jpg", "https://product.kyobobook.co.kr/detail/S000001550353", "N", 50000, 5000, 45000, "1", "1");
        OrderProductDto orderProductDto2 = new OrderProductDto(2, -1, "101", "AVBL", "301", "N", "[국내도서] 토비의 스프링 3.1 세트", 1, "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788960773431.jpg", "https://product.kyobobook.co.kr/detail/S000000935360", "N", 75000, 7500, 67500, "1", "1");
        orderProductDtoList.add(orderProductDto);
        orderProductDtoList.add(orderProductDto2);
        return orderProductDtoList;
    }
}