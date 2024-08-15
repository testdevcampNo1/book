package com.no1.book.dao.order;

import com.no1.book.domain.order.OrderStatusHistoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderStatusHistoryDao {
    // create
    void createOrderStatusHistory(OrderStatusHistoryDto orderStatusHistoryDto);

    // read
    OrderStatusHistoryDto getOrderStatusHistory(int ordStusHistorySeq);
    List<OrderStatusHistoryDto> getCustomerOrderStatusHistory(int custId);
    List<OrderStatusHistoryDto> getOrderStatusHistoryByOrdId(String ordId);
    List<OrderStatusHistoryDto> getAllOrderStatusHistory();

    // delete
    void deleteOrderStatusHistory(String ordId);
    void deleteAllOrderStatusHistory();
}
