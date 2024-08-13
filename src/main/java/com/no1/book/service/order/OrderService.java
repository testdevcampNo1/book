package com.no1.book.service.order;

import com.no1.book.domain.order.OrderFormDto;
import com.no1.book.domain.order.OrderProductDto;

import java.util.List;

public interface OrderService {
    OrderFormDto initOrderInfo(int custId, List<OrderProductDto> productList);
    void saveOrder(OrderFormDto orderInfo);
    int getOrderId();
    void saveOrderProduct(List<OrderProductDto> productList);
    void saveOrderStatus();
    void saveDelivery();
    void savePayment();

//    void order();
//    void insertCustInfo();
//    void insertAddressInfo();
}
