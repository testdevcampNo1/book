package com.no1.book.dao.order;

import com.no1.book.domain.order.CartDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface CartDao {

    List<CartDto> selectCart(Integer custId) throws Exception;
    Integer insertItem(CartDto cartDto) throws Exception;
    Integer updateItemQty(CartDto cartDto) throws Exception;
    Integer deleteItem(Map map) throws Exception;
    Integer deleteAll() throws Exception;

}
