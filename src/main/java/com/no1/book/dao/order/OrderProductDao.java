package com.no1.book.dao.order;

import com.no1.book.domain.order.OrderProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderProductDao {
    // create
    void insertOrderProduct(OrderProductDto orderProductDto);

    // read
    OrderProductDto getOrderProduct(int ordProdId);
    List<OrderProductDto> getCustomerOrderProducts(int custId);
    List<OrderProductDto> getOrderProductsByOrderId(int ordId);
    List<OrderProductDto> getAllOrderProduct();

    // update
    void updateOrderProductStatus(Map params);

    // delete
    void deleteOrderProducts(int ordId);
    void deleteCustomerOrderProducts(int custId);
    void deleteAllOrderProduct();
}
