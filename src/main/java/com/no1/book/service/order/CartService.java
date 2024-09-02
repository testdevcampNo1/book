package com.no1.book.service.order;

import com.no1.book.domain.order.CartDto;
import com.no1.book.domain.order.CartProdDto;

import java.util.List;
import java.util.Map;

public interface CartService {
    List<CartProdDto> read(String custId) throws Exception;

    Integer insertItem(CartDto cartDto) throws Exception;

    Integer updateItemQty(CartDto cartDto) throws Exception;

    Integer remove(Map map) throws Exception;
}
