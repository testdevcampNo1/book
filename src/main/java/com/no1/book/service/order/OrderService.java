package com.no1.book.service.order;

public interface OrderService {
    void saveOrder();
    void saveOrderProduct();
    void saveOrderStatus();
    void saveDelivery();
    void savePayment();
    int getOrderId();
    void insertCustInfo();
    void insertAddressInfo();
}
