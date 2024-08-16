package com.no1.book.service.order;

import com.no1.book.domain.order.OrderProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface OrderHistoryService {
    Map<String, List<OrderProductDto>> getCustomerOrderHistoryList();
    OrderProductDto getOrderProductDto(int ordProdId);
    void cancelOrder(int ordProdId);
}
