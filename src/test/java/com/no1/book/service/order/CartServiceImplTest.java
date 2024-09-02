package com.no1.book.service.order;

import com.no1.book.dao.order.CartDao;
import com.no1.book.domain.order.CartDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceImplTest {
    @Autowired
    CartService cartService;

    @Autowired
    CartDao cartDao;

    @Test
    public void selectCart() {

    }

    @Test
    public void insertItem() {
    }

    @Test
    public void updateItemQty() {
        // cartDao.deleteAll();

        // CartDto cartDto = new CartDto(1,"PROD001")
        // Integer r = cartService.updateItemQty;
        // assertTrue(r>0);
    }

    @Test
    public void deleteItem() throws Exception {

        Map map = new HashMap();
        map.put("custId","1");
        map.put("postId","PROD002");
        Integer row = cartService.remove(map);
        assertTrue(row>0);
    }
}