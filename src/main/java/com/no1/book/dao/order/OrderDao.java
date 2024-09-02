package com.no1.book.dao.order;

import com.no1.book.domain.order.OrderDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDao {
    // create
    void createOrder(OrderDto orderDto);

    // read
    OrderDto getOrder(String ordId);
    List<OrderDto> getCustomerOrders(String custId);
    List<OrderDto> getAllOrder();

    // update
    void updateOrderStatus(Map param);

    // delete
    void deleteCustomerOrders(String custId);
    void deleteAllOrder();
}
