package com.no1.book.dao.order;

import com.no1.book.domain.order.CartProdDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface CartProdDao {

    List<CartProdDto> selectCartItem(String custId);
}
