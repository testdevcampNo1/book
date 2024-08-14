package com.no1.book.service.order;

import com.no1.book.domain.order.CartDto;
import com.no1.book.domain.order.CartProdDto;

import java.util.List;
import java.util.Map;

public interface CartService {
    List<CartProdDto> read(Integer custId) throws Exception;

    int insertItem(CartDto cartDto) throws Exception;

    int updateItemQty(CartDto cartDto) throws Exception;

    Integer remove(Map map) throws Exception;
}
