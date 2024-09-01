package com.no1.book.dao.order;

import com.no1.book.domain.order.CartDto;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;


/* 장바구니 테스트
* 장바구니 상품 수량 조회
* 장바구니 상품 조회
* 장바구니 상품 등록일 조회
* 장바구니 상품 저장 만료일 조회 - 만료일이 지나면 상품 삭제
*
* 장바구니 상품 추가 - 기존에 상품이 있으면 수량만 업데이트
* 장바구니 상품 수량 업데이트 - 1이상의 값만 가능
* 장바구니 상품 삭제 - 회원ID, 상품ID 두 개가 반드시 일치하는 것만 삭제
*
* selectItemTest - 상품 조회
* deleteItemTest - 상품 삭제
* insertItemTest - 상품 추가
*
* */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartDaoTest {

    @Autowired
    CartDao cartDao;

    @Test
    @DisplayName("장바구니 상품 목록 조회")
    public void selectCartItemTest() throws Exception{
        //cartDao.deleteAll();

        Integer custId = 1;
        Integer fakeCustId = 10;
        CartDto cartDto;
        for (int i = 0; i <10 ; i++) {
            cartDto = new CartDto(custId,"PROD00"+i,1);
            cartDao.insertItem(cartDto);
        }

        // 성공) 회원ID가 존재할 때 -> 1
        // System.out.println(cartDao.selectCartItem(1).size());
        assertTrue(cartDao.selectCart(custId).size() >0);

        // 실패) 회원ID가 존재하지 않을 때 -> 0
        assertTrue(cartDao.selectCart(fakeCustId).size() == 0);

    }


    @Test
    @DisplayName("장바구니 상품 추가 - Param:CartDto")
    public void insertItemTest() throws Exception{
        cartDao.deleteAll();

        // 성공) 각 타입 정상적으로 들어갈 경우 -> 1
        CartDto cartDto = new CartDto(9, "PROD009" , 1);
        //System.out.println("cartDao.insertItem(cartDto) = " + cartDao.insertItem(cartDto));
        assertTrue(cartDao.insertItem(cartDto) == 1);

        // 실패) 이미 상품이 존재한다면 -> 0
        CartDto cartDto2 = new CartDto(9, "PROD009" , 2);
        System.out.println("cartDao.insertItem(cartDto2) = " + cartDao.insertItem(cartDto2));
        assertTrue(cartDao.insertItem(cartDto2) == 0);

        // 예외) DataIntegrityViolationException -> 빈 Dto 에러, 중복키 값 들어갈 경우
        try{
            CartDto cartDto1 = new CartDto();
            System.out.println("cartDao.insertItem(cartDto1) = " + cartDao.insertItem(cartDto1));
        }catch(Exception e){
            e.printStackTrace();
        }


        // 예외) 중복 키 값이 들어갈 때
        CartDto cartDto3 = new CartDto(9, "PROD009" , 1);
        CartDto cartDto4 = new CartDto(9, "PROD009" , 1);

        assertTrue(cartDao.insertItem(cartDto3)==1);
        assertTrue(cartDao.insertItem(cartDto4)==0);

    }

    @Test
    @DisplayName("장바구니 수량 업데이트")
    public void updateItemQtyTest() throws Exception{
        cartDao.deleteAll();


        CartDto cartDto = new CartDto(1, "PROD0001",1 );
        assertTrue(cartDao.insertItem(cartDto)==1);

        // 성공) 일치 -> 1
        CartDto cartDto1 = new CartDto(1,"PROD0001",2);
        assertTrue(cartDao.updateItemQty(cartDto1) == 1);


        // 실패1) 테이블에 없는 값 -> 0
        CartDto cartDto2 = new CartDto(1,"PROD0003",2);
        assertTrue(cartDao.updateItemQty(cartDto2) == 0);

        // 실패2) key값 불일치 -> 0
        CartDto cartDto3 = new CartDto(2,"PROD0003",2);
        assertTrue(cartDao.updateItemQty(cartDto3) == 0);

        // 실패3) 값이 없는 map -> 0
        CartDto cartDto4 = new CartDto();
        System.out.println("cartDao.updateItemQty(cartDto4) = " + cartDao.updateItemQty(cartDto4));
        assertTrue(cartDao.updateItemQty(cartDto4) == 0);


    }



    @Test
    @DisplayName("장바구니 상품 삭제")
    public void deleteItemTest() throws Exception{
        cartDao.deleteAll();

        CartDto cartDto = new CartDto(10, "PROD3897",2);
        assertTrue(cartDao.insertItem(cartDto)==1);

        // 성공)
        Map map = new HashMap();
        map.put("custId",cartDto.getCustId());
        map.put("prodId",cartDto.getProdId());

        assertTrue(cartDao.deleteItem(map)==1);

        // 실패1) 회원ID 일치, 상품ID 불일치
        Map map3 = new HashMap();
        map3.put("custId",10);
        map3.put("prodId","dddd");
        assertTrue(cartDao.deleteItem(map3) == 0);

        // 실패2) 회원ID, 상품ID 불일치
        Map map2 = new HashMap();
        assertTrue(cartDao.deleteItem(map2)==0);

    }

}