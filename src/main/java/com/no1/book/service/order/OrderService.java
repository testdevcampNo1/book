package com.no1.book.service.order;

import com.no1.book.domain.order.OrderFormDto;
import com.no1.book.domain.order.OrderProductDto;

import java.util.List;

public interface OrderService {
    OrderFormDto initOrderInfo(String custId, List<OrderProductDto> productList) throws Exception;
    void requestOrder(OrderFormDto orderFormDto);
    void saveOrder(OrderFormDto orderInfo);
    void saveOrderProduct(String ordId, List<OrderProductDto> productList);
    void saveOrderStatus(String ordId);
    void saveDelivery(String ordId);
    void savePayment(String ordId);

//    void order();
//    void insertCustInfo();
//    void insertAddressInfo();
}
