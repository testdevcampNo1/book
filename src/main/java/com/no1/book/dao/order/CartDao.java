package com.no1.book.dao.order;

import com.no1.book.domain.order.CartDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface CartDao {

    List<CartDto> selectCart(Integer custId) throws Exception;

    int insertItem(CartDto cartDto) throws Exception;
    int updateItemQty(CartDto cartDto) throws Exception;
    Integer deleteItem(Map map) throws Exception;
    int deleteAll() throws Exception;
   }
