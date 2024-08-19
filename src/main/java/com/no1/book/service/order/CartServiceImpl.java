package com.no1.book.service.order;

import com.no1.book.dao.order.CartDao;
import com.no1.book.dao.order.CartProdDao;
import com.no1.book.domain.order.CartDto;
import com.no1.book.domain.order.CartProdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartDao cartDao;

    @Autowired
    CartProdDao cartProdDao;

    @Override
    public List<CartProdDto> read(Integer custId) throws Exception{
        List<CartProdDto> cartProdDto = cartProdDao.selectCartItem(custId);
        return cartProdDto;

    }

    @Override
    public Integer insertItem(CartDto cartDto) throws Exception{
        return cartDao.insertItem(cartDto);
    }

    @Override
    public Integer updateItemQty(CartDto cartDto) throws Exception{
        return cartDao.updateItemQty(cartDto);
    }

    @Override
    public Integer remove(Map map) throws Exception{
        return cartDao.deleteItem(map);
    }


}
