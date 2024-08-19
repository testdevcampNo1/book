package com.no1.book.service.product;

import com.no1.book.dao.product.ProductDao;
import com.no1.book.domain.product.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProductServiceImplTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDao productDao;


////     가격 기준 정렬 테스트1 (내림차순)
//    @Test
//    void sortItemsByPrice1() throws Exception {
//        // 1단계 데이터 선택 -> 임의의 30개 상품을 담고있는 리스트
//            // 1-1 딘계 : 데이터 비우기
//        productDao.deleteAll();
//            // 1-2 단계 : 임의의 30개 상품 채우기 (가격 제각각으로)
//        for (int i = 1; i < 31; i++) {
//            ProductDto dto = ProductDto.builder()
//                    .prodId("PROD" + i)
//                    .prodBasePrice(5000 * ((int)(Math.random() * 19) + 1)) // 가격 : 5,000원 ~ 100,000원 사이 5,000단위 랜덤
//                    .build() ;
//            productDao.insert(dto);
//        }
//            // 1-3 단계 : 상품들을 리스트로 반환
//        List<ProductDto> prodList = productDao.selectAll();
//
//        // 2단계 데이터 처리 -> 가격 기준 내림차순 정렬
//        prodList = productService.sortItemsByPrice(0); // 0 : 내림차순, 1 : 오름차순
//
//        // 3단계 검증 1 -> 일단 출력을 눈으로 확인해보자
//        for (int i = 0; i < prodList.size(); i++) {
//            System.out.println("상품 id : " + prodList.get(i).getProdId() + " 가격 : " + prodList.get(i).getProdBasePrice());
//        }
//
//        // 3단계 검증 2 -> 반복문을 돌면서 현재 가격이 이전 가격보다 크지 않으면 통과
//        int tmp = 100000000;
//        for (int i = 0; i < prodList.size(); i++) {
//            int thisPrice = prodList.get(i).getProdBasePrice();
//            assertEquals(true, thisPrice <= tmp);
//            tmp = thisPrice;
//        }
//    }
//
//    // 가격 기준 정렬 테스트1 (오름차순)
//    @Test
//    void sortItemsByPrice2() throws Exception {
//        // 1단계 데이터 선택 -> 임의의 30개 상품을 담고있는 리스트
//        // 1-1 딘계 : 데이터 비우기
//        productDao.deleteAll();
//        // 1-2 단계 : 임의의 30개 상품 채우기 (가격 제각각으로)
//        for (int i = 1; i < 31; i++) {
//            ProductDto dto = ProductDto.builder()
//                    .prodId("PROD" + i)
//                    .prodBasePrice(5000 * ((int)(Math.random() * 19) + 1)) // 가격 : 5,000원 ~ 100,000원 사이 5,000단위 랜덤
//                    .build() ;
//            productDao.insert(dto);
//        }
//        // 1-3 단계 : 상품들을 리스트로 반환
//        List<ProductDto> prodList = productDao.selectAll();
//
//        // 2단계 데이터 처리 -> 가격 기준 내림차순 정렬
//        prodList = productService.sortItemsByPrice(1); // 0 : 내림차순, 1 : 오름차순
//
//        // 3단계 검증 1 -> 일단 출력을 눈으로 확인해보자
//        for (int i = 0; i < prodList.size(); i++) {
//            System.out.println("상품 id : " + prodList.get(i).getProdId() + " 가격 : " + prodList.get(i).getProdBasePrice());
//        }
//
//        // 3단계 검증 2 -> 반복문을 돌면서 현재 가격이 이전 가격보다 작지 않으면
//        int tmp = 0;
//        for (int i = 0; i < prodList.size(); i++) {
//            int thisPrice = prodList.get(i).getProdBasePrice();
//            assertEquals(true, thisPrice >= tmp);
//            tmp = thisPrice;
//        }
//    }



}