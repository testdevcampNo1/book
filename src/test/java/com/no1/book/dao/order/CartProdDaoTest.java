package com.no1.book.dao.order;

import com.no1.book.domain.order.CartProdDto;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartProdDaoTest {

    @Autowired
    CartProdDao cartProdDao;

    @Test
    @DisplayName("장바구니 상품 정보")
    public void selectItemDetailTest() throws Exception{

        String custId = "1";
        List<CartProdDto> cartProducts = cartProdDao.selectCartItem(custId);
        System.out.println("cartProducts = " + cartProducts.size());

        // Then: 조회된 결과가 예상대로인지 확인
        assertThat(cartProducts).isNotEmpty();
        assertThat(cartProducts.size()).isGreaterThan(0);

        CartProdDto firstCartProduct = cartProducts.get(0);

        // 첫 번째 항목에 대해 검증 수행
        assertThat(firstCartProduct.getCustId()).isEqualTo(custId);
        assertThat(firstCartProduct.getProdName()).isNotBlank();
        assertThat(firstCartProduct.getSalePrice()).isGreaterThanOrEqualTo(0);
        assertThat(firstCartProduct.getItemQty()).isGreaterThan(0);

    }

}